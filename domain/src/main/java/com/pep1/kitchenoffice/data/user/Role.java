package com.pep1.kitchenoffice.data.user;

import com.pep1.kitchenoffice.data.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.security.core.GrantedAuthority;

@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role extends AbstractPersistable implements GrantedAuthority {

    @Indexed
    private String roleName;


    public String getAuthority() {
        return roleName;
    }

}
