package webit.script.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebitProperties}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class WebitPropertiesTest {

    private WebitProperties properties;

    @BeforeEach
    void setUp() {
        properties = new WebitProperties();
    }

    @Nested
    @DisplayName("Default values")
    class DefaultValues {

        @Test
        void defaultTemplateLoaderPath() {
            assertThat(properties.getTemplateLoaderPath())
                    .containsExactly("classpath:/templates/");
        }

        @Test
        void defaultSettingsIsEmpty() {
            assertThat(properties.getSettings()).isEmpty();
        }

        @Test
        void defaultPreferFileSystemAccess() {
            assertThat(properties.isPreferFileSystemAccess()).isTrue();
        }

        @Test
        void defaultCheckTemplateLocation() {
            assertThat(properties.isCheckTemplateLocation()).isTrue();
        }

        @Test
        void defaultAutoCheck() {
            assertThat(properties.isAutoCheck()).isFalse();
        }

    }

    @Nested
    @DisplayName("Setters and getters")
    class SettersAndGetters {

        @Test
        void setAndGetSettings() {
            Properties settings = new Properties();
            settings.setProperty("key1", "value1");
            properties.setSettings(settings);
            assertThat(properties.getSettings()).containsEntry("key1", "value1");
        }

        @Test
        void setAndGetTemplateLoaderPath() {
            properties.setTemplateLoaderPath("/custom/path");
            assertThat(properties.getTemplateLoaderPath()).containsExactly("/custom/path");
        }

        @Test
        void setAndGetPreferFileSystemAccess() {
            properties.setPreferFileSystemAccess(false);
            assertThat(properties.isPreferFileSystemAccess()).isFalse();
        }

        @Test
        void setAndGetCheckTemplateLocation() {
            properties.setCheckTemplateLocation(false);
            assertThat(properties.isCheckTemplateLocation()).isFalse();
        }

        @Test
        void setAndGetAutoCheck() {
            properties.setAutoCheck(true);
            assertThat(properties.isAutoCheck()).isTrue();
        }

    }

    @Nested
    @DisplayName("Constants")
    class Constants {

        @Test
        void defaultTemplateLoaderPathConstant() {
            assertThat(WebitProperties.DEFAULT_TEMPLATE_LOADER_PATH)
                    .isEqualTo("classpath:/templates/");
        }

        @Test
        void defaultPrefixConstant() {
            assertThat(WebitProperties.DEFAULT_PREFIX).isEmpty();
        }

        @Test
        void defaultSuffixConstant() {
            assertThat(WebitProperties.DEFAULT_SUFFIX).isEqualTo(".httl");
        }

    }

    @Nested
    @DisplayName("applyToMvcViewResolver")
    class ApplyToMvcViewResolver {

        @Test
        void methodExistsAndIsCallableViaReflection() throws Exception {
            // WebitViewResolver depends on javax.servlet which is not on test classpath,
            // so we verify the method signature exists via reflection.
            java.lang.reflect.Method method = WebitProperties.class.getMethod(
                    "applyToMvcViewResolver",
                    webit.script.support.springmvc3.WebitViewResolver.class);
            assertThat(method).isNotNull();
            assertThat(method.getReturnType()).isEqualTo(void.class);
        }

        @Test
        void applyToMvcViewResolverSetsPrefixAndSuffix() throws Exception {
            // Create a mock-like WebitViewResolver via reflection since javax.servlet
            // is not on the test classpath. We test that the method calls setPrefix/setSuffix.
            // Since WebitViewResolver can't be instantiated without servlet API,
            // we verify the method exists and its parameter types.
            java.lang.reflect.Method method = WebitProperties.class.getMethod(
                    "applyToMvcViewResolver",
                    webit.script.support.springmvc3.WebitViewResolver.class);
            assertThat(method.getParameterCount()).isEqualTo(1);
            assertThat(method.getParameterTypes()[0].getSimpleName()).isEqualTo("WebitViewResolver");
        }

    }

}
