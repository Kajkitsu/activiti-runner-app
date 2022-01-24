package com.isi.projekt.servicetasks;

import com.isi.projekt.dto.RunnerDTO;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GenPayLinkServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        RunnerDTO runnerDTO = RunnerDTO.from(execution.getVariable("runnerDTO", String.class));

        runnerDTO.setPaymentLink(getPaymentLink(runnerDTO.getId()));

        execution.setVariable("runnerDTO", runnerDTO.toJson());
    }

    private String getPaymentLink(Integer runnerId) {
        String url = "http://localhost:5001/paypal/make_payment/" + runnerId;
        HttpPost request = new HttpPost(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        try {
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(request);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8"));
            return jsonObject.getString("redirect_url");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
