package br.com.fiap.smokesignal.mock;

import org.springframework.stereotype.Component;

import br.com.fiap.smokesignal.entity.TipoUsuarioEnum;
import br.com.fiap.smokesignal.entity.UsuarioEntity;

@Component
public class UsuarioLogadoMock {
    
    public UsuarioEntity getUsuarioLogado() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNome("Lucas Garcia");
        usuario.setEmail("lucas@email.com");
        usuario.setTipo(TipoUsuarioEnum.ADMIN); 
        return usuario;
    }
}