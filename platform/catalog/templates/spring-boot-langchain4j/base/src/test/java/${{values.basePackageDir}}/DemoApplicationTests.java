package ${{ values.basePackage }};

import org.junit.jupiter.api.Test;
{%- if values.llmProvider == "openai" %}
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
{%- endif %}
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

{% if values.llmProvider == "openai" %}
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
{%- endif %}
@SpringBootTest
@Import(TestDemoApplication.class)
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
