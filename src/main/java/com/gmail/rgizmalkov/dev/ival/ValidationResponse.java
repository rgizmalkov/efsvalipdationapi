package com.gmail.rgizmalkov.dev.ival;

import com.gmail.rgizmalkov.dev.meta.Validation;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class ValidationResponse {
    private String title;
    private String message;
    private Status status;
    private List<ValidationResponse> caused;

    public ValidationResponse() {
    }

    public ValidationResponse(String title, String message, Status status) {
        this.title = title;
        this.message = message;
        this.status = status;
    }

    public static ValidationResponse baseFailedAnswer(Class<?> cls){
        return new ValidationResponse("Failed.", format("Validation of class [%s] instance was failed!",cls), Status.FAILED);
    }

    public static ValidationResponse baseWarnAnswer(Class<?> cls){
        return new ValidationResponse("Warning.", format("Validation of class [%s] instance was ended with warning(s).",cls), Status.WARN);
    }

    public static ValidationResponse baseOkAnswer(Class<?> cls){
        return new ValidationResponse("Ok.", format("Validation of class [%s] instance is ok.",cls), Status.OK);
    }

    public void addCause(ValidationResponse response){
        if(caused == null) caused = new ArrayList<>();

    }



    public ValidationResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public ValidationResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public ValidationResponse setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Validation {\n")
                .append("\tTitle: ").append(title).append(",]\n")
                .append("\tMessage: ").append(message).append(",\n")
                .append("\tStatus: ").append(status).append(",");
        sb.append("\tcaused by: [");
        int iter = 1;
        for (ValidationResponse validationResponse : caused) {
            sb.append("\t\t").append(iter++).append(". ").append(validationResponse.title)
                    .append("[").append(validationResponse.message).append(", ").append(status.toString().toUpperCase()).append("]\n");
        }
        sb.append("\t]\n}\n");
        return sb.toString();
    }
}
