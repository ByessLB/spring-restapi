package fr.afpa.restapi.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.afpa.restapi.dao.AccountDao;
import fr.afpa.restapi.model.Account;

/**
 *  ajouter la/les annotations nécessaires pour faire de
 * "AccountRestController" un contrôleur de REST API
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {
    private final AccountDao accountDao;

    /**
     * implémenter un constructeur
     * 
     * injecter {@link AccountDao} en dépendance par injection via constructeur
     * Plus d'informations ->
     * https://keyboardplaying.fr/blogue/2021/01/spring-injection-constructeur/
     */

    public AccountRestController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * implémenter une méthode qui traite les requêtes GET et qui renvoie une
     * liste de comptes
     */
    @GetMapping
    public List<Account> getAllAccounts() {
        List<Account> listAccount = accountDao.findAll();
        return listAccount;
    }

    /**
     * implémenter une méthode qui traite les requêtes GET avec un identifiant
     * "variable de chemin" et qui retourne les informations du compte associé
     * Plus d'informations sur les variables de chemin ->
     * https://www.baeldung.com/spring-pathvariable
     */
    // @GetMapping("api/accounts")
    // public Optional<Account> getAccount(@RequestParam long param) {
    // Optional<Account> account = accountDao.findById(param);
    // return account;
    // }

    /* ou */

    // Solution sans gestion des erreurs
    // @GetMapping("/{param}")
    // public Optional<Account> getAccount(@PathVariable long param) {
    // Optional<Account> account = accountDao.findById(param);
    // return account;
    // }

    @GetMapping("/{param}")
    public ResponseEntity<?> getAccount(@PathVariable long param) {

        Optional<Account> accountOptional = accountDao.findById(param);
        if (accountOptional.isPresent()) {
            return new ResponseEntity<Account>(accountOptional.get(), HttpStatus.OK);
        } else {
            return new ErrorResponseEntity();
        }
    }

    /**
     *  implémenter une méthode qui traite les requêtes POST
     * Cette méthode doit recevoir les informations d'un compte en tant que "request
     * body", elle doit sauvegarder le compte en mémoire et retourner ses
     * informations (en json)
     * Tutoriel intéressant -> https://stackabuse.com/get-http-post-body-in-spring/
     * Le serveur devrai retourner un code http de succès (201 Created)
     **/

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Account entities) {
        Account createdAccount = accountDao.save(entities);
        if (createdAccount != null) {
            return new ResponseEntity<Account>(createdAccount, HttpStatus.CREATED);
        } else {
            return new ErrorResponseEntity();
        }
    }

    /**
     *  implémenter une méthode qui traite les requêtes PUT
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account entities) {
        entities.setId(id);
        Account updatedAccount = accountDao.save(entities);
        if (updatedAccount != null) {
            return new ResponseEntity<Account>(updatedAccount, HttpStatus.NO_CONTENT);
        }
        return new ErrorResponseEntity();
    }

    /**
     *  implémenter une méthode qui traite les requêtes DELETE
     * L'identifiant du compte devra être passé en "variable de chemin" (ou "path
     * variable")
     * Dans le cas d'un suppression effectuée avec succès, le serveur doit retourner
     * un status http 204 (No content)
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAccount(@PathVariable Long id){
        Optional<Account> tmpAccount = accountDao.findById(id);
        Account toBeDeletedAccount = tmpAccount.get();
        if (toBeDeletedAccount != null) {
            accountDao.delete(toBeDeletedAccount);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ErrorResponseEntity();
        }
    }
}
