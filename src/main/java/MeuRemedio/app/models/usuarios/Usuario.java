package MeuRemedio.app.models.usuarios;


import MeuRemedio.app.models.agendamentos.AgendamentosHorarios;
import MeuRemedio.app.models.remedios.Remedio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Usuario implements UserDetails {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "US_ID", nullable = false)
    private long id;

    @Column(name = "US_Nome", nullable = false)
    @NotNull @NotEmpty @NotBlank
    private String nome;

    @Column(name = "US_Sobrenome", nullable = false)
    @NotNull @NotEmpty @NotBlank
    private String sobrenome;

    @Column(name = "US_Email", nullable = false)
    @NotNull @NotEmpty @NotBlank @Email
    private String email;

    @Column(name = "US_Senha", nullable = false)
    @NotNull @NotBlank @Size(min = 8)
    private String senha;

    @Column(name = "US_DataNascimento", nullable = false)
    @NotNull @NotEmpty @NotBlank
    private String dataNascimento;

    @Column(name = "US_Sexo", nullable = false)
    @NotNull @NotBlank @NotEmpty
    private String sexo;

    @Column(name = "Criado_em")
    @NotNull
    private LocalDate criadoEm;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;


    @OneToMany
    private List <Remedio> remedio;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<UsuarioNotificationToken> usuarioNotificationTokens;


    public Usuario (String nome, String sobrenome, String email, String senha, String dataNascimento,
                   String sexo/* List<Remedio> remedios, List<Financeiro> financeiro */) {

        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.criadoEm = LocalDate.now();
       // this.remedios = remedios;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
