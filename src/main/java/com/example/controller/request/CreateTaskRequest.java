package com.example.controller.request;

import com.example.model.Priority;
import com.example.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Шаблон задачи(Task) без ID")
public class CreateTaskRequest {

    @NotBlank(message = "Heading cannot be blank")
    @Schema(description = "Заголовок задачи")
    private String heading;

    @NotBlank(message = "Description cannot be blank")
    @Schema(description = "Описание задачи")
    private String description;

    @NotNull(message = "Status cannot be null")
    @Schema(description = "Статус задачи")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Priority cannot be null")
    @Schema(description = "Приоритет задачи")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotBlank(message = "Name of executor cannot be null")
    @Schema(description = "Имя исполнителя")
    private String executorName;

}
