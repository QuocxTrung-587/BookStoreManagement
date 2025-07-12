package vn.tdtu.finalterm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject<M> {
    private String status;
    private String message;
    private M data;
}
