package com.bantads.auth;

import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bantads.auth.Rabbit.Message;
import com.bantads.auth.Rabbit.QueueSender;
import com.bantads.auth.entity.Usuario;
import com.bantads.auth.entity.UsuarioDTO;
import com.bantads.auth.entity.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@RestController
@EnableJpaRepositories
@RequestMapping("/auth")
public class Controller {
	
	
	@Autowired
	public UsuarioRepository repo;
	
	@Autowired
	private ModelMapper mapper; 
	
	@Autowired
	private QueueSender queueSender;
	
	@RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
		ObjectMapper mapper = new ObjectMapper();
        Gson gson = new Gson();
        
        Message message = gson.fromJson(fileBody, Message.class);
        try {
        	ObjectNode node =  mapper.readValue(message.getData(), ObjectNode.class);
			UsuarioDTO usuarioDTO = gson.fromJson(message.getData(), UsuarioDTO.class);
			if (message.getOperacao().equals("new")) {
				if (!message.getEstorno()) {
					UsuarioDTO result = this.newUsuario(usuarioDTO);
					if (result==null) {
						message.setErro("Usuario já cadastrado");
						message.setCodErro(409);
					} else {
						JsonNode usuaNode = node.get("cliente");
						((ObjectNode) usuaNode).put("usua_codigo", result.getCodigo());
						message.setData(node.toString());
					}
				} else {
					this.revertNewUsuario(usuarioDTO);
				}
			}
        } catch (Exception e) {
			e.printStackTrace();
		}
        this.queueSender.send(message);
    }
	
	private UsuarioDTO getUsuario(String login) {
		return this.repo.findByLogin(login);
	}
	
	@PostMapping("/login")
	ResponseEntity<UsuarioDTO> login(@RequestBody UsuarioDTO usuarioIn) {
		UsuarioDTO usuaResult = null;
		try {	
			UsuarioDTO usuarioDto = this.getUsuario(usuarioIn.getLogin());
			if (usuarioDto!=null) {
				Usuario usuario = mapper.map(usuarioDto, Usuario.class);
//				if (usuarioIn.getSenha().equals(Base64.getDecoder().decode(usuario.getSenha()))) {
				if (usuarioIn.getSenha().equals(usuario.getSenha())) {	
					usuarioIn.setCodigo(usuario.getCodigo());
					usuarioIn.setRole(usuario.getRole());
					usuaResult = usuarioIn;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			usuaResult = null;
		}
		return ResponseEntity.ok().body(usuaResult);
	}
	
	@PostMapping("/new")
	ResponseEntity<UsuarioDTO> newUsuarioRequest(@RequestBody UsuarioDTO usuarioIn) {
		UsuarioDTO usuarioDTO = this.newUsuario(usuarioIn);
		if (usuarioDTO==null) {
			return ResponseEntity.status(409).build();
		}
		return ResponseEntity.ok().body(usuarioDTO);
	}
	
	public UsuarioDTO newUsuario(UsuarioDTO usuarioIn) {
		UsuarioDTO usuaResult = null;
		try {	
			UsuarioDTO usuarioDto = this.getUsuario(usuarioIn.getLogin());
			if (usuarioDto==null) {
				usuarioIn.setSenha(Base64.getEncoder().encodeToString(usuarioIn.getSenha().getBytes()));
				this.repo.save(usuarioIn);
				usuaResult = usuarioIn;	
			}
		} catch (Exception e) {
			e.printStackTrace();
			usuaResult = null;
		}
		return usuaResult;
	}
	
	private void revertNewUsuario(UsuarioDTO usuarioIn) {
		try {	
			Integer id = this.repo.findByLogin(usuarioIn.getLogin()).getCodigo();
			this.repo.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
