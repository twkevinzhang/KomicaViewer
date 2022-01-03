package self.nesl.komicaviewer.paragraph;

import java.io.Serializable;

public class Paragraph implements Serializable {
    private String content;
    private ParagraphType type;

    public String getContent() {
        return content;
    }

    public ParagraphType getType() {
        return type;
    }

    public Paragraph(String content, ParagraphType type){
        this.content=content;
        this.type=type;
    }
}

