package br.com.fiap.smokesignal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.smokesignal.entity.TipoUsuarioEnum;
import br.com.fiap.smokesignal.entity.UsuarioEntity;
import br.com.fiap.smokesignal.mock.UsuarioLogadoMock;

@Component
public class PermissaoValidator {

    @Autowired
    private UsuarioLogadoMock usuarioLogado;

    public void validarAdmin() {
        UsuarioEntity usuario = usuarioLogado.getUsuarioLogado();
        if (usuario.getTipo() != TipoUsuarioEnum.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ação permitida apenas para ADMIN");
        }
    }

    public void validarAdminOuAnalista() {
        UsuarioEntity usuario = usuarioLogado.getUsuarioLogado();
        if (usuario.getTipo() != TipoUsuarioEnum.ADMIN && usuario.getTipo() != TipoUsuarioEnum.ANALISTA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ação permitida apenas para ADMIN ou ANALISTA");
        }
    }
}
