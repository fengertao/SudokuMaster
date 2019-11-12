package charlie.feng.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import java.text.SimpleDateFormat;

public class JacksonObjectMapper {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        mapper.enable(SerializationFeature.INDENT_OUTPUT).setDateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm"));
        return mapper;
    }
}
