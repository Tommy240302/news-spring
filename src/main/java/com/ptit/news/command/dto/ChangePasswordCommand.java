package com.ptit.news.command.dto;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordCommand {
    @TargetAggregateIdentifier
    private String email; 
    private String oldPassword;
    private String newPassword;
}
