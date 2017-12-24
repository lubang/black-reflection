package net.lulab.black;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlackReflectionUtil {

    private static final Map<Class<?>, Object> DEFAULT_VALUES;
    private static final List<String> IGNORE_NAMES = Arrays.asList("class");
    // ref. https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html

    static {
        DEFAULT_VALUES = new HashMap<>();
        DEFAULT_VALUES.put(byte.class, (byte) 0);
        DEFAULT_VALUES.put(short.class, (short) 0);
        DEFAULT_VALUES.put(int.class, 0);
        DEFAULT_VALUES.put(long.class, 0L);
        DEFAULT_VALUES.put(float.class, 0.f);
        DEFAULT_VALUES.put(double.class, 0.d);
        DEFAULT_VALUES.put(char.class, '\u0000');
        DEFAULT_VALUES.put(boolean.class, false);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createDefaultValue(Class<T> type) {
        return (T) DEFAULT_VALUES.getOrDefault(type, null);
    }

    /**
     * Get method by getter function.
     *
     * @param clazz  Class type
     * @param getter Function
     * @param <T>    Class type
     * @param <R>    Return type
     * @return Method
     */
    public static <T, R> Method getMethod(Class<?> clazz, Function<T, R> getter) {
        final AtomicReference<Method> handledMethod = new AtomicReference<>();
        final InvocationHandler invocationHandler = (proxy, method, args) -> {
            handledMethod.getAndSet(method);
            return createDefaultValue(method.getReturnType());
        };
        final Class<?> proxyClazz = createProxyClass(clazz, invocationHandler);
        final T proxy = createMockInstance(proxyClazz);

        getter.apply(proxy);
        return handledMethod.get();
    }

    /**
     * get name of Bean by getter function.
     *
     * @param clazz  Class type
     * @param getter Function
     * @param <T>    Class type
     * @param <R>    Return type
     * @return Bean name
     */
    public static <T, R> String getBeanName(Class<?> clazz, Function<T, R> getter) {
        final Method method = getMethod(clazz, getter);
        final String methodName = method.getName();

        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            return Introspector.decapitalize(methodName.substring(3));
        }
        if (methodName.startsWith("is")) {
            return Introspector.decapitalize(methodName.substring(2));
        }

        throw new BlackReflectionException("Method is not bean style: " + methodName);
    }

    private static <T> Class<? extends T> createProxyClass(Class<T> clazz,
                                                           InvocationHandler invocationHandler) {
        return new ByteBuddy()
                .subclass(clazz)
                .method(ElementMatchers.isMethod())
                .intercept(InvocationHandlerAdapter.of(invocationHandler))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
    }

    @SuppressWarnings("unchecked")
    private static <T> T createMockInstance(Class<?> proxyClazz) {
        try {
            final Constructor<?> constructor = proxyClazz.getConstructors()[0];
            final Object[] defaults = createDefaultParameters(constructor.getParameterTypes());
            return (T) constructor.newInstance(defaults);
        } catch (Exception e) {
            throw new BlackReflectionException(e.getMessage(), e);
        }
    }

    private static Object[] createDefaultParameters(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(BlackReflectionUtil::createDefaultValue)
                .toArray();
    }

    /**
     * Get all bean names of the class.
     *
     * @param clazz Class type
     * @return names
     */
    public static List<String> getBeanNames(Class<?> clazz) {
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            final PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            return Arrays.stream(pds)
                    .map(FeatureDescriptor::getName)
                    .filter(p -> !IGNORE_NAMES.contains(p))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BlackReflectionException(e.getMessage(), e);
        }
    }
}
