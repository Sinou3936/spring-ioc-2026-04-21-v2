package com.ll.framework.ioc;

import com.ll.framework.ioc.annotations.Component;
import com.ll.framework.ioc.annotations.Configuration;
import com.ll.framework.ioc.annotations.Repository;
import com.ll.framework.ioc.annotations.Service;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final String basePackage;
    private final Map<String, Object> beans = new HashMap<>();
    private final Map<String, Class<?>> beanDefinitons = new HashMap<>();

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void init() {
        String packagePath = basePackage.replace(".","/");
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(packagePath);

        File directory = new File(resource.getFile());
        List<Class<?>> classes = new ArrayList<>();

        findClassess(directory, basePackage,classes);

        for(Class<?> clazz : classes)
        {
            if(clazz.isAnnotationPresent(Component.class)
            ||clazz.isAnnotationPresent(Configuration.class)
            ||clazz.isAnnotationPresent(Repository.class)
            ||clazz.isAnnotationPresent(Service.class)
            ){
                Object instance = createInstance(clazz);
                String beanName = lowerFirst(clazz.getSimpleName());
                beans.put(beanName,instance);
            }


        }

    }

    private String lowerFirst(String simpleName) {
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    private Object createInstance(Class<?> clazz) {
        try{
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            Object[] args = new Object[parameterTypes.length];

            for(int i=0; i<args.length; i++){
                args[i] = getBeanByType(parameterTypes[i]);
            }

            return constructor.newInstance(args);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Object getBeanByType(Class<?> parameterType) {
        for(Object bean : beans.values())
        {
            if(parameterType.isInstance(bean))
            {
                return bean;
            }
        }
        return null;
    }

    private void findClassess(File directory, String basePackage, List<Class<?>> classes) {
        File[] files = directory.listFiles();

        if(files == null) return;

        for(File file : files){

            if(file.isDirectory()) {
                String subPackage = basePackage + "." + file.getName();
                findClassess(file, subPackage, classes);
            }
            else if(file.getName().endsWith(".class"))
            {
                String className = file.getName().replace(".class","");
                String fullClassName = basePackage+"."+className;

                try{
                    Class<?> clazz = Class.forName(fullClassName);
                    if(clazz.getName().contains("domain")){
                        classes.add(clazz);
                    }
                }catch (ClassNotFoundException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public <T> T genBean(String beanName) {
//        Class<?> clazz = beanDefinitons.get(beanName);
//        return (T) createInstance(clazz);
        return (T) beans.get(beanName);
    }
}
