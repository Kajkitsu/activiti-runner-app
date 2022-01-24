package com.isi.projekt.servicetasks;

import java.util.Collections;
import java.util.Optional;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeparacjaMailiServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            Map<Integer, String> cities = Optional.ofNullable(delegateExecution.getVariable("miasta",Map.class)).orElse(Collections.emptyMap());
            Map<Integer, String> mails =  Optional.ofNullable(delegateExecution.getVariable("maile",Map.class)).orElse(Collections.emptyMap());
            List<String> tempMails = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : mails.entrySet()) {
                tempMails.add(entry.getValue());
            }

            List<String> repeatedMails = repeatedEmails(tempMails);
            Map<String, String> uniqueRunners = new HashMap<>();
            for (Map.Entry<Integer, String> entry : mails.entrySet()) {
                Integer key = entry.getKey();
                String city = cities.get(key);
                for (String repeatedMail : repeatedMails) {
                    if(!uniqueRunners.isEmpty()){
                        uniqueRunners.remove(entry.getValue());
                    }
                    if (entry.getValue().equals(repeatedMail)) {
                        break;
                    }
                    if (uniqueRunners.containsKey(entry.getValue())) {
                        break;
                    } else {
                        uniqueRunners.put(entry.getValue(), city);
                    }
                }
            }

            for (String re : repeatedMails) {
                System.out.println("Powtórzony mail: " + re);
            }
            delegateExecution.setVariable("powtorzenia", repeatedMails);
            delegateExecution.setVariable("biegacze", uniqueRunners);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> repeatedEmails(List<String> emails) {
        List<String> existedMail = new ArrayList<>();
        String pattern = "";
        for (int i = 0; i < emails.size(); i++) {
            pattern = emails.get(i);
            for (int j = 0; j < emails.size(); j++) {
                if (i == j) {
                    break;
                }
                if (emails.get(j).equals(pattern)) {
                    if (existedMail.contains(emails.get(i))) {
                        break;
                    } else {
                        existedMail.add(emails.get(i));
                    }
                }
            }
        }
        return existedMail;
    }
}
