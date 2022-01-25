package org.example.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AIRequest {
    private List<String> linkNews;
    private List<String> linkVideo;
    private List<String> linkImage;
    private String content;
    private boolean is_debug;
}
