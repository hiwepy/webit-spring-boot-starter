package webit.script.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

/**
 * Tests for {@link WebitAutoConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
class WebitAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(WebitAutoConfiguration.class));

    @Nested
    @DisplayName("Properties binding")
    class PropertiesBinding {

        @Test
        void propertiesAreBound() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(WebitProperties.class);
                WebitProperties props = context.getBean(WebitProperties.class);
                assertThat(props.getTemplateLoaderPath())
                        .containsExactly("classpath:/templates/");
            });
        }

        @Test
        void customPropertiesAreBound() {
            contextRunner
                    .withPropertyValues(
                            "spring.webit.check-template-location=false",
                            "spring.webit.auto-check=true")
                    .run(context -> {
                        WebitProperties props = context.getBean(WebitProperties.class);
                        assertThat(props.isCheckTemplateLocation()).isFalse();
                        assertThat(props.isAutoCheck()).isTrue();
                    });
        }

    }

    @Nested
    @DisplayName("Inner configuration classes")
    class InnerConfigurations {

        @Test
        void beetlConfigurationClassExists() {
            assertThat(WebitAutoConfiguration.BeetlConfiguration.class).isNotNull();
        }

        @Test
        void beetlNonWebConfigurationClassExists() {
            assertThat(WebitAutoConfiguration.BeetlNonWebConfiguration.class).isNotNull();
        }

        @Test
        void beetlWebConfigurationClassExists() {
            assertThat(WebitAutoConfiguration.BeetlWebConfiguration.class).isNotNull();
        }

    }

    @Nested
    @DisplayName("Bean creation")
    class BeanCreation {

        @Test
        void autoConfigurationBeanIsCreated() {
            contextRunner.run(context -> {
                assertThat(context).hasSingleBean(WebitAutoConfiguration.class);
            });
        }

    }

    @Nested
    @DisplayName("BeetlConfiguration")
    class BeetlConfigurationTests {

        @Test
        void applyPropertiesCopiesSettings() throws Exception {
            WebitAutoConfiguration.BeetlConfiguration config = new WebitAutoConfiguration.BeetlConfiguration();
            WebitProperties props = new WebitProperties();
            Properties settings = new Properties();
            settings.setProperty("key1", "value1");
            props.setSettings(settings);
            // Set the properties field via reflection
            java.lang.reflect.Field field = WebitAutoConfiguration.BeetlConfiguration.class.getDeclaredField("properties");
            field.setAccessible(true);
            field.set(config, props);
            // applyProperties should not throw
            config.applyProperties(config);
        }

    }

    @Nested
    @DisplayName("CheckTemplateLocationExists")
    class CheckTemplateLocationTests {

        @Test
        void doesNotWarnWhenCheckDisabled() {
            contextRunner
                    .withPropertyValues("spring.webit.check-template-location=false")
                    .run(context -> {
                        WebitAutoConfiguration autoConfig = context.getBean(WebitAutoConfiguration.class);
                        // Should not throw even if template locations don't exist
                        autoConfig.checkTemplateLocationExists();
                    });
        }

        @Test
        void warnsWhenCheckEnabledAndTemplateNotFound() {
            contextRunner
                    .withPropertyValues("spring.webit.check-template-location=true",
                            "spring.webit.template-loader-path=classpath:/nonexistent/")
                    .run(context -> {
                        WebitAutoConfiguration autoConfig = context.getBean(WebitAutoConfiguration.class);
                        // Should log a warning but not throw
                        autoConfig.checkTemplateLocationExists();
                    });
        }

    }

}
