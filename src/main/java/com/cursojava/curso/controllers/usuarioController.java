package com.cursojava.curso.controllers;

import com.cursojava.curso.dao.UsuarioDao;
import com.cursojava.curso.models.Usuario;
import com.cursojava.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class usuarioController {

    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private JWTUtil jwtUtil;
    @RequestMapping(value = "api/usuario/{id}", method = RequestMethod.GET)
    public Usuario getUsuario(@PathVariable Long id){
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Manuel");
        usuario.setApellido("Alvarado");
        usuario.setEmail("manuelalvarado3113@gmail.com");
        usuario.setTelefono("76387812");
        return usuario;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario){

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash= argon2.hash(1,1024,1, usuario.getPassword());
        usuario.setPassword(hash);
        usuarioDao.registrar(usuario);
    }


    @RequestMapping(value = "api/usuarios")
    public List<Usuario> getUsuario(@RequestHeader(value = "Authorization")String token){
        if (!validarToken(token)){return null;}


        return usuarioDao.getUsuarios();
    }

    private boolean validarToken(String token){
        String idusuario = jwtUtil.getKey(token);
        return idusuario != null;
    }

    @RequestMapping(value = "usuario1")
    public Usuario editar(){
        Usuario usuario = new Usuario();
        usuario.setNombre("Manuel");
        usuario.setApellido("Alvarado");
        usuario.setEmail("manuelalvarado3113@gmail.com");
        usuario.setTelefono("76387812");
        return usuario;
    }
    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void eliminar(@RequestHeader(value = "Authorization")String token,@PathVariable Long id){
        if (!validarToken(token)){return;}
        usuarioDao.eliminar(id);

    }

}
