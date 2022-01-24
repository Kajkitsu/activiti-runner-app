package com.isi.projekt;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ActivitiController {
    public static final String PAYMENT_USER_TASK = "sid-DCE5CE90-B34E-4B91-897C-F09D021E3AAC";
    private static final Logger logger = LoggerFactory.getLogger(ActivitiController.class);
    private static final String WELCOME_PACK_TASK = "sid-C205AA56-36A3-44F0-80B6-1D492EEBB15A";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/start-process/{email}")
    public String startProcess(@PathVariable String email) {
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("my-process", Map.of("email", email));
        return String.format("process instance ID: %s process variables: %s ", processInstance.getProcessInstanceId(), processInstance.getProcessVariables());
    }

    @GetMapping("/get_tasks/{processInstanceId}")
    public List<TaskRepresentation> getTasks(@PathVariable String processInstanceId) {
        List<Task> usertasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        return usertasks.stream()
                .map(task -> new TaskRepresentation(task.getId(), task.getName(), task.getProcessInstanceId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/get_all_tasks")
    public List<TaskRepresentation> getAllTasks() {
        return taskService.createTaskQuery()
                .list()
                .stream()
                .map(task -> new TaskRepresentation(task.getId(), task.getName(), task.getProcessInstanceId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/complete_payment/{processInstanceId}")
    public void completePaymentTask(@PathVariable String processInstanceId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (task.getTaskDefinitionKey().equals(PAYMENT_USER_TASK)) {
            taskService.setVariable(task.getId(), "payed", true);
            taskService.complete(task.getId());
            logger.info("Task set as payed");
        }
    }

    @GetMapping("/uncomplete_payment/{processInstanceId}")
    public void uncompletePaymentTask(@PathVariable String processInstanceId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (task.getTaskDefinitionKey().equals(PAYMENT_USER_TASK)) {
            taskService.setVariable(task.getId(), "payed", false);
            taskService.complete(task.getId());
            logger.info("Task set as not payed");
        }

    }

    @GetMapping("/send_welcome_pack/{processInstanceId}")
    public void sendWelcomePack(@PathVariable String processInstanceId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (task.getTaskDefinitionKey().equals(WELCOME_PACK_TASK)) {
            taskService.complete(task.getId());
            logger.info("Task set as send payed");
        }
    }


}
