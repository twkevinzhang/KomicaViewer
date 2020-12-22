package self.nesl.komicaviewer.repository;

import android.util.Log;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.request.OnResponse;
import self.nesl.komicaviewer.request.Request;

public class BoardRepository implements Repository<Board> {
    private Request<List<Board>> request;

    public void setRequest(Request<List<Board>> request) {
        this.request = request;
    }

    @Override
    public void getAll(OnResponse<List<Board>> onResponse) {
        request.fetch(onResponse);
    }

    @Override
    public void get(String li_link, OnResponse<Board> onResponse) {
        getAll(boards->{
            onResponse.onResponse(boards.stream().filter(b -> b.getId().equals(li_link)).findFirst().get());
        });
    }
}
