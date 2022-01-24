package com.isi.projekt.servicetasks;

import com.isi.projekt.dto.RunnerDTO;
import com.isi.projekt.dto.RunnerResponseDTO;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

public class DownloadForEmailServiceTask implements JavaDelegate {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(DelegateExecution execution) {

        Optional<String> email = Optional.ofNullable(execution.getVariable("email", String.class));
        if (email.isPresent()) {
            Optional<RunnerDTO> runnerDTO = getRunners(email.get());
            runnerDTO.ifPresent(dto -> execution.setVariable("runnerDTO", dto.toJson()));
        }
    }

    private Optional<RunnerDTO> getRunners(String email) {
        String url = "http://localhost:5001/runners/" + email;
        HttpGet request = new HttpGet(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        try {
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(request);
            return Optional.ofNullable(RunnerDTO.from(objectMapper
                    .readValue(EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8"), RunnerResponseDTO.class)));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
