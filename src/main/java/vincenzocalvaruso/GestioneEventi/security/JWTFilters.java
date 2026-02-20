package vincenzocalvaruso.GestioneEventi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.exceptions.UnauthorizedException;
import vincenzocalvaruso.GestioneEventi.service.UserService;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilters extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Questo è il metodo che viene eseguito a ogni richiesta
        // Sarà questo metodo quindi che dovrà fare il controllo dei token

        // PIANO DI BATTAGLIA

        // 1. Verifichiamo se la richiesta contiene l'header Authorization e che in caso sia nel formato "Bearer oi1j3oj21o3j213jo12j3"
        // Se l'header non c'è oppure se è malformato --> lanciamo eccezione
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'Authorization header nel formato corretto");

        // 2. Estrarre il token dall'header, diversi metodi di manipolazione di stringhe
        //  authHeader -> "Bearer oi1j3oj21o3j213jo12j3"  <-- vogliamo prendere tutto tranne "Bearer " <- ATTENZIONE ALLO SPAZIO
        String accessToken = authHeader.replace("Bearer ", "");

        // 3. Verifichiamo se il token è valido (controllare la firma e verificare data di scadenza)
        jwtTools.verificaToker(accessToken);

        // *************************************************** AUTORIZZAZIONE **************************************************
        // Ora posso verificare il ruolo dell'utente cosi da capire se è autorizzato o meno a fare una determinata azione
        //Per farlo seguo i seguenti passaggi

        // 1. Cerchiamo l'utente nel DB tramite id (l'id sta nel token!)
        // 1.1 Leggiamo l'id dal token
        UUID userId = jwtTools.extractIdFromToken(accessToken);

        // 1.2 Find by Id
        User authenticatedDip = this.userService.findById(userId);
        // 2. Associamo l'utente al Security Context
        //Step fondamentale, cosi facendo ovunqe ci troviamo (altri filtri, il controller, un endpoint...) si potrà risalire
        // chi è l'utente che ha effettuato l'azione
        // Questo è molto utile per ad esempio controllare i ruoli dell'utente prima di arrivare ad uno specifico endpoint.
        // Oppure ci può servire
        //  per effettuare determinati controlli all'interno degli endpoint stessi per verificare che chi stia
        //  facendo una certa operazione di lettura/modifica/cancellazione sia l'effettivo proprietario della risorsa,
        //  oppure per, in fase di creazione di una risorsa, associare l'effettivo proprietario a tale risorsa.

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedDip, null, authenticatedDip.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 4. Se tutto è OK --> andiamo avanti, trasmettiamo la richiesta al prossimo (può essere o un altro elemento della catena oppure il controller)
        filterChain.doFilter(request, response);

        // 5. Se c'è qualche problema con il token -> eccezione
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/user/**", request.getServletPath());
        // return request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/register");
    }
}
