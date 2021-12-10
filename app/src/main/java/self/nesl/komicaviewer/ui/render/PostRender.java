package self.nesl.komicaviewer.ui.render;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.ui.thread.ReplyDialog;

public class PostRender extends SpannableStringRender{
    private Post post;
    private List<Post> posts;
    private TextView textView;
    private SpannableStringBuilder strBuilder;
    private OnReplyToClickListener onReplyToClickListener;

    public PostRender(Post post, List<Post> posts, TextView textView){
        super(textView);
        this.post=post;
        this.posts=posts;
        this.textView=textView;
        this.strBuilder = new SpannableStringBuilder();
    }

    @Override
    public SpannableStringBuilder render() {
        if(post.getQuote() != null) addQuote(post.getQuote());
        if(post.getReplyTo() != null) addReplyFor(post.getReplyTo());
        if(post.getText() != null) addText(post.getText());
        return strBuilder;
    }

    public void addQuote(String quote){
        new LinkBuilder(strBuilder).addLink(quote, ()->{
            Toast.makeText(textView.getContext(), quote, Toast.LENGTH_SHORT).show();
        });
    }

    public void addReplyFor(String replyTo){
        Optional<Post> result= posts.stream().filter(b -> b.getId().equals(replyTo)).findFirst();
        if(result.isPresent()){
            Post replyFor= result.get();
            String preview = MessageFormat.format("{0} ({1})", replyTo, replyFor.getDescription(10));
            new LinkBuilder(strBuilder).addLink(preview, ()-> onReplyToClickListener.onReplyToClickListener(replyFor, posts));
        }else{
            new LinkBuilder(strBuilder).addLink(replyTo, null);
        }
    }

    public void addText(String text){
        strBuilder.append(text);
    }

    public void setReplyToOnClickListener(OnReplyToClickListener onReplyToClickListener){
        this.onReplyToClickListener=onReplyToClickListener;
    }

    public interface OnReplyToClickListener{
        void onReplyToClickListener(Post replyTo, List<Post> list);
    }
}

