package com.example.backend.chatgpt;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatGptResponseDto implements Serializable {

    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private List<ChatGptChoice> choices;
    private String text; //챗지피티 답변 내용!!!

    @Builder
    public ChatGptResponseDto(String id, String object,
                              LocalDate created, String model,
                              List<ChatGptChoice> choices ,String text) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.model = model;
        this.choices = choices;
        this.text = text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChatGptResponseDto{");
        sb.append("id='").append(id).append('\'');
        sb.append(", object='").append(object).append('\'');
        sb.append(", created=").append(created);
        sb.append(", model='").append(model).append('\'');
        sb.append(", choices=[");
        if (choices != null) {
            //choice 안의 text(응답내용) 출력
            for (ChatGptChoice choice : choices) {
                sb.append("{text='").append(choice.getText()).append('\'');
                sb.append(", index=").append(choice.getIndex());
                sb.append(", finish_reason='").append(choice.getFinishReason()).append('\'');
                sb.append("}, ");
            }
            sb.deleteCharAt(sb.length() - 2); // 마지막 쉼표와 공백 제거

        }
        sb.append("]");
        sb.append('}');
        return sb.toString();
    }
}
