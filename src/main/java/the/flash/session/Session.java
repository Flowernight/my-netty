package the.flash.session;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xulh on 2019/10/28.
 */
@Data
@NoArgsConstructor
public class Session {

    private String userId;

    private String userName;

    public Session(String userId, String userName){
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userId + ":" + userName;
    }
}
