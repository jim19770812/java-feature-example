package org.jim.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.springframework.util.Assert;

@EqualsAndHashCode
@Accessors(chain = true, fluent = true)
public class JContextEntity {
    /**
     * 键
     */
    @Getter @Setter
    @NonNull
    private String k;

    /**
     * 值
     */
    @Getter
    private Object v;

    @NonNull
    public JContextEntity v(@NonNull Object value){
        Assert.isTrue(!(value instanceof JContextEntity), String.format("发现在设置上下文变量[%s]时，设置了ITContextEntity的值，不允许这样做", this.k));
        this.v=value;
        return this;
    }

    /**
     * 复制新的实例
     * @return
     */
    @NonNull
    public JContextEntity copyInstance(){
        return new JContextEntity().k(this.k).v(this.v);
    }

    @Override
    public String toString() {
        return new StringBuilder(this.k).append("=").append(this.v).toString();
    }
}
