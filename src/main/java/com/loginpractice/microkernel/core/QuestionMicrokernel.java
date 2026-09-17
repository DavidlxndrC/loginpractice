package com.loginpractice.microkernel.core;

import com.loginpractice.microkernel.common.entities.GeneratedQuestion;
import com.loginpractice.microkernel.common.interfaces.IQuestionPlugin;
import com.loginpractice.microkernel.common.entities.QuestionRequest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;


public class QuestionMicrokernel {

    private static final String PLUGINS_FILE = "plugins.properties";
    private static final String PROPERTY_PREFIX = "question.";

    private final Map<Integer, GeneratedQuestion> questions = new LinkedHashMap<>();
    private final Map<String, IQuestionPlugin> activePlugins = new LinkedHashMap<>();
    private final Properties pluginProperties = new Properties();

    public QuestionMicrokernel() {
        loadProperties();
        registerAllPlugins();
    }

    private void loadProperties() {

        try (InputStream stream = QuestionMicrokernel.class.getClassLoader().getResourceAsStream(PLUGINS_FILE)) {

            if (stream == null) {
                throw new IllegalStateException("No se encontró " + PLUGINS_FILE + " en el classpath.");
            }

            pluginProperties.load(stream);

        } catch (IOException ex) {
            throw new IllegalStateException("Error al leer " + PLUGINS_FILE, ex);
        }
    }

    private void registerAllPlugins() {

        for (String propertyName : pluginProperties.stringPropertyNames()) {

            if (!propertyName.startsWith(PROPERTY_PREFIX)) {
                continue;
            }

            String type = propertyName.substring(PROPERTY_PREFIX.length()).toUpperCase();
            String pluginClassName = pluginProperties.getProperty(propertyName);

            loadPlugin(type, pluginClassName);
        }
    }

    
     // Carga y registra dinámicamente un plugin a partir del nombre de su clase usando Reflexión.
    private void loadPlugin(String type, String pluginClassName) {

        try {

            Class<?> pluginClass = Class.forName(pluginClassName);
            Object pluginObject = pluginClass.getDeclaredConstructor().newInstance();

            if (pluginObject instanceof IQuestionPlugin plugin) {
                activePlugins.put(type, plugin);
            }

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                 | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException("No se pudo cargar el plugin " + pluginClassName + " por reflexión.", ex);
        }
    }

    // Ciclo de vida: permite descargar (desactivar) un plugin en tiempo de ejecución sin afectar a los demás
    public void unloadPlugin(String type) {
        activePlugins.remove(type.toUpperCase());
    }

    // Ciclo de vida: permite volver a registrar un plugin ya conocido en plugins.properties
    public void reloadPlugin(String type) {

        String propertyName = PROPERTY_PREFIX + type.toLowerCase();
        String pluginClassName = pluginProperties.getProperty(propertyName);

        if (pluginClassName == null) {
            throw new IllegalArgumentException("No existe un plugin configurado para el tipo: " + type);
        }

        loadPlugin(type.toUpperCase(), pluginClassName);
    }

    public IQuestionPlugin getPlugin(String type) {

        if (type == null) {
            return null;
        }

        return activePlugins.get(type.toUpperCase());
    }

    public Set<String> getRegisteredTypes() {
        return Collections.unmodifiableSet(activePlugins.keySet());
    }

    // Ejecuta el plugin correspondiente al tipo de la solicitud y devuelve la pregunta generada
    public GeneratedQuestion generateQuestion(QuestionRequest request) {

        IQuestionPlugin plugin = getPlugin(request.getType());

        if (plugin == null) {
            throw new IllegalArgumentException("No hay un plugin activo para el tipo: " + request.getType());
        }

        return plugin.generate(request);
    }

    /**
     Registra en el banco de preguntas del núcleo una pregunta ya generada, 
     usando el mismo id que le asignó la persistencia real (QuestionService.createQuestion)
     */
    public void registerGenerated(int id, GeneratedQuestion generated) {
        questions.put(id, generated);
    }

    public Optional<GeneratedQuestion> findGenerated(int id) {
        return Optional.ofNullable(questions.get(id));
    }

    public List<GeneratedQuestion> getAllGenerated() {
        return List.copyOf(questions.values());
    }
}
