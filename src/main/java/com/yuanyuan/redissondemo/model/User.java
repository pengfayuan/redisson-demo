package com.yuanyuan.redissondemo.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author FAYUAN.PENG
 * @version \$Id: User.java,  2021-05-15 16:45 FAYUAN.PENG Exp $$
 */
@Data
public class User implements Serializable {

    private Integer id;
    private String name;
}
