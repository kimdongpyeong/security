package kr.supporti.api.app.dto;

import java.util.List;

import javax.mail.util.ByteArrayDataSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    private String from;

    private List<String> toList;

    private String subject;

    private String text;

    private List<String> ccList;

    private String fileName;

    private ByteArrayDataSource fileData;
}
