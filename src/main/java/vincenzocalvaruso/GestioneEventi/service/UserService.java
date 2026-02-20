package vincenzocalvaruso.GestioneEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.GestioneEventi.entity.Role;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.exceptions.NotFoundException;
import vincenzocalvaruso.GestioneEventi.exceptions.UnauthorizedException;
import vincenzocalvaruso.GestioneEventi.payloads.LoginDTO;
import vincenzocalvaruso.GestioneEventi.payloads.RegisterDTO;
import vincenzocalvaruso.GestioneEventi.repository.UserRepository;
import vincenzocalvaruso.GestioneEventi.security.JWTTools;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTTools jwtTools;

    public User register(RegisterDTO dto) {
        // Verifica se l'email esiste già
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Errore: Questa email è già utilizzata.");
        }

        // Crea l'entità User dal DTO
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password())); // In futuro: BCrypt qui
        user.setRole(dto.role() != null ? dto.role() : Role.USER);

        return userRepository.save(user);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public String checkCredenzialAndReturnToken(LoginDTO body) {
        //1- controllo che esiste un utente con quella email, se esiste controllo che la password sia uguale
        //Se credenziali non ok genero exception --> 401 UnAuthorized
        User found = userRepository.findByEmail(body.email()).orElseThrow();
        // if (found.getPassword().equalsIgnoreCase(body.password())) {
        // TODO: LA PASSWORDO COSI è MOMENTANEA, MIGLIORERò CIò
        if (passwordEncoder.matches(body.password(), found.getPassword())) {

            //2- creo token
            String token = jwtTools.generaToken(found);

            return token;


        } else {
            throw new UnauthorizedException("Credenziali non valide");
        }


    }
}
