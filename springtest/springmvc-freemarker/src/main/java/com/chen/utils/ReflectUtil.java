package com.chen.utils;

import org.springframework.asm.*;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: wei
 * DateTime: 2016-02-11
 */
public class ReflectUtil {
    private static class ParameterNameDiscoveringVisitor extends ClassVisitor {
        private static final String STATIC_CLASS_INIT = "<clinit>";
        private final Class<?> clazz;
        private final Map<Member, String[]> memberMap;
        public ParameterNameDiscoveringVisitor(Class<?> clazz, Map<Member, String[]> memberMap) {
            super(SpringAsmInfo.ASM_VERSION);
            this.clazz = clazz;
            this.memberMap = memberMap;
        }
        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            // exclude synthetic + bridged && static class initialization
            if (!isSyntheticOrBridged(access) && !STATIC_CLASS_INIT.equals(name)) {
                return new LocalVariableTableVisitor(clazz, memberMap, name, desc, isStatic(access));
            }
            return null;
        }
        private static boolean isSyntheticOrBridged(int access) {
            return (((access & Opcodes.ACC_SYNTHETIC) | (access & Opcodes.ACC_BRIDGE)) > 0);
        }
        private static boolean isStatic(int access) {
            return ((access & Opcodes.ACC_STATIC) > 0);
        }
    }
    private static class LocalVariableTableVisitor extends MethodVisitor {
        private static final String CONSTRUCTOR = "<init>";
        private final Class<?> clazz;
        private final Map<Member, String[]> memberMap;
        private final String name;
        private final Type[] args;
        private final boolean isStatic;
        private String[] parameterNames;
        private boolean hasLvtInfo = false;
        /*
         * The nth entry contains the slot index of the LVT table entry holding the
         * argument name for the nth parameter.
         */
        private final int[] lvtSlotIndex;
        public LocalVariableTableVisitor(Class<?> clazz, Map<Member, String[]> map, String name, String desc,
                                         boolean isStatic) {
            super(SpringAsmInfo.ASM_VERSION);
            this.clazz = clazz;
            this.memberMap = map;
            this.name = name;
            // determine args
            args = Type.getArgumentTypes(desc);
            this.parameterNames = new String[args.length];
            this.isStatic = isStatic;
            this.lvtSlotIndex = computeLvtSlotIndices(isStatic, args);
        }
        @Override
        public void visitLocalVariable(String name, String description, String signature, Label start, Label end,
                                       int index) {
            this.hasLvtInfo = true;
            for (int i = 0; i < lvtSlotIndex.length; i++) {
                if (lvtSlotIndex[i] == index) {
                    this.parameterNames[i] = name;
                }
            }
        }
        @Override
        public void visitEnd() {
            if (this.hasLvtInfo || (this.isStatic && this.parameterNames.length == 0)) {
                // visitLocalVariable will never be called for static no args methods
                // which doesn't use any local variables.
                // This means that hasLvtInfo could be false for that kind of methods
                // even if the class has local variable info.
                memberMap.put(resolveMember(), parameterNames);
            }
        }
        private Member resolveMember() {
            ClassLoader loader = clazz.getClassLoader();
            Class<?>[] classes = new Class<?>[args.length];
            // resolve args
            for (int i = 0; i < args.length; i++) {
                classes[i] = ClassUtils.resolveClassName(args[i].getClassName(), loader);
            }
            try {
                if (CONSTRUCTOR.equals(name)) {
                    return clazz.getDeclaredConstructor(classes);
                }
                return clazz.getDeclaredMethod(name, classes);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException("Method [" + name
                        + "] was discovered in the .class file but cannot be resolved in the class object", ex);
            }
        }
        private static int[] computeLvtSlotIndices(boolean isStatic, Type[] paramTypes) {
            int[] lvtIndex = new int[paramTypes.length];
            int nextIndex = (isStatic ? 0 : 1);
            for (int i = 0; i < paramTypes.length; i++) {
                lvtIndex[i] = nextIndex;
                if (isWideType(paramTypes[i])) {
                    nextIndex += 2;
                } else {
                    nextIndex++;
                }
            }
            return lvtIndex;
        }
        private static boolean isWideType(Type aType) {
            // float is not a wide type
            return (aType == Type.LONG_TYPE || aType == Type.DOUBLE_TYPE);
        }
    }

    /**
     * 获取 方法 参数列表中 参数名的方法
     * @param clazz
     * @return
     * @throws IOException
     */
    public static Map<Member,String[]>getArgumentParam(Class<?> clazz) throws IOException {
        InputStream is = clazz.getResourceAsStream(ClassUtils.getClassFileName(clazz));
        ClassReader classReader = new ClassReader(is);
        Map<Member, String[]> map = new ConcurrentHashMap<Member, String[]>(32);
        classReader.accept(new ParameterNameDiscoveringVisitor(clazz, map), 0);
        return map;
    }
}
