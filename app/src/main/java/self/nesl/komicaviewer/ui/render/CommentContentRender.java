package self.nesl.komicaviewer.ui.render;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import self.nesl.komicaviewer.models.Comment;
import self.nesl.komicaviewer.paragraph.Paragraph;

public class CommentContentRender implements Render{
    LinearLayout root;
    private Comment comment;

    public CommentContentRender(Context context, Comment comment){
        this.root =new LinearLayout(context);
        this.comment=comment;
    }

    public View render(){
        if(!comment.getContent().isEmpty()) {
            for (Paragraph paragraph :comment.getContent()) {
                switch (paragraph.getType()){
                    case String:
                        addText(paragraph.getContent());
                        break;
                }
            }
        }
        return root;
    }

    void addText(String text){
        CommentContentRender.RenderTool tool= new CommentContentRender.RenderTool(root.getContext());
        root.addView(tool.renderText(text));
    }

    class RenderTool {
        private Context context;
        RenderTool(Context context){
            this.context=context;
        }

        private View renderText(String text){
            TextView textView = new TextView(context);
            textView.setText(text);
            return textView;
        }
    }
}

