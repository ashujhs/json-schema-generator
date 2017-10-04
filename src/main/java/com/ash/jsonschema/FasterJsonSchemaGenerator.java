package com.ash.jsonschema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by agupt12 on 1/23/17 for ofrresend
 *
 *
 */
public class FasterJsonSchemaGenerator {

    private static final XLogger logger = XLoggerFactory.getXLogger(FasterJsonSchemaGenerator.class);
    private   ObjectMapper serializeMapper;

    public void jsonSchema(File directory, String packageName, File classesPath, ClassLoader classLoader ) {
        try {
            logger.info("init");
            initSerializeMapper();
            // configure mapper, if necessary, then create schema generator
            serializeMapper.configure(
                    com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
                    true);

            JsonSchemaGenerator generator = new JsonSchemaGenerator(serializeMapper);
            SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();

            JsonSchema jsonSchema =null;

            List<Class> files = new ArrayList<>();
            Path path= Paths.get(classesPath.toURI());
            List<Class> fileNames = getFileNames(files, path,packageName);
            for(Class clazz : getClasses(packageName, classLoader)){
                jsonSchema = generator.generateSchema(clazz);
                String fileName = clazz.getSimpleName()+".json";
                System.out.println(serializeMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));
                fileWriter(serializeMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema), fileName, directory);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private  void initSerializeMapper() {
        serializeMapper = new ObjectMapper();
        serializeMapper.setVisibilityChecker(serializeMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
        serializeMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        serializeMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    public static void fileWriter(String data, String jSonFileName, File directory){
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {


            File file = new File(directory,jSonFileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.write(data);

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }
    private static Class[] getClasses(String packageName, ClassLoader classLoader)
            throws ClassNotFoundException, IOException {

        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String str=resource.getFile();
            dirs.add(new File(str));
            logger.info("Going to look into {}",str);
        }
        File currentDir = new File(".");
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, classLoader));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @param classLoader
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName,
            ClassLoader classLoader) throws
            ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName(), classLoader));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private List<Class> getFileNames(List<Class> fileNames, Path dir, String packageName) {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if(path.toFile().isDirectory()) {
                    assert !path.toFile().getName().contains(".");
                    getFileNames(fileNames, path,packageName);
                } else {
                    try {
                        if(path.toFile().toString().endsWith(".class")){
                            logger.info("file path {}",path.toAbsolutePath());
                            String filePath = path.toAbsolutePath().toString().replace('/', '.');
                            if(StringUtils.isNotEmpty(filePath) ){
                                //String classFile = filePath.substring(filePath.indexOf(packageName),filePath.lastIndexOf(".class"));
                                String classFile = FilenameUtils.removeExtension(filePath);
                                        fileNames.add(Class.forName(classFile));
                            }
                            System.out.println(path.getFileName());
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }
}
