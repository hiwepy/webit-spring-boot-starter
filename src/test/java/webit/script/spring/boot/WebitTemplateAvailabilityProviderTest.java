package webit.script.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebitTemplateAvailabilityProvider}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
class WebitTemplateAvailabilityProviderTest {

    @Nested
    @DisplayName("Provider instantiation")
    class ProviderInstantiation {

        @Test
        void canBeInstantiated() {
            WebitTemplateAvailabilityProvider provider = new WebitTemplateAvailabilityProvider();
            assertThat(provider).isNotNull();
        }

    }

    @Nested
    @DisplayName("JetbrickTemplateAvailabilityProperties")
    class JetbrickTemplateAvailabilityPropertiesTests {

        @Test
        void canBeInstantiated() throws Exception {
            Class<?> clazz = Class.forName(
                    "webit.script.spring.boot.WebitTemplateAvailabilityProvider$JetbrickTemplateAvailabilityProperties");
            Object instance = clazz.getDeclaredConstructor().newInstance();
            assertThat(instance).isNotNull();
        }

        @Test
        void defaultLoaderPath() throws Exception {
            Class<?> clazz = Class.forName(
                    "webit.script.spring.boot.WebitTemplateAvailabilityProvider$JetbrickTemplateAvailabilityProperties");
            Object instance = clazz.getDeclaredConstructor().newInstance();
            java.lang.reflect.Method getLoaderPath = clazz.getDeclaredMethod("getLoaderPath");
            getLoaderPath.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<String> loaderPath = (List<String>) getLoaderPath.invoke(instance);
            assertThat(loaderPath).containsExactly(WebitProperties.DEFAULT_TEMPLATE_LOADER_PATH);
        }

        @Test
        void getAndSetTemplateLoaderPath() throws Exception {
            Class<?> clazz = Class.forName(
                    "webit.script.spring.boot.WebitTemplateAvailabilityProvider$JetbrickTemplateAvailabilityProperties");
            Object instance = clazz.getDeclaredConstructor().newInstance();

            java.lang.reflect.Method getter = clazz.getMethod("getTemplateLoaderPath");
            java.lang.reflect.Method setter = clazz.getMethod("setTemplateLoaderPath", List.class);

            @SuppressWarnings("unchecked")
            List<String> originalPath = (List<String>) getter.invoke(instance);
            assertThat(originalPath).containsExactly(WebitProperties.DEFAULT_TEMPLATE_LOADER_PATH);

            setter.invoke(instance, List.of("/custom/path"));
            @SuppressWarnings("unchecked")
            List<String> updatedPath = (List<String>) getter.invoke(instance);
            assertThat(updatedPath).containsExactly("/custom/path");
        }

    }

}
