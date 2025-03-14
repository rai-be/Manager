package com.gerenciador.user_service.controller;



import com.gerenciador.user_service.notificationClient.ScheduledTaskNotifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/tasks")
    public class ScheduledTaskController {

        private final ScheduledTaskNotifier scheduledTaskNotifier;

        public ScheduledTaskController(ScheduledTaskNotifier scheduledTaskNotifier) {
            this.scheduledTaskNotifier = scheduledTaskNotifier;
        }

        @GetMapping("/check-pending")
        public String triggerTaskCheck() {
            scheduledTaskNotifier.checkPendingTasks();
            return "🔍 Verificação de tarefas pendentes iniciada!";
        }
    }


