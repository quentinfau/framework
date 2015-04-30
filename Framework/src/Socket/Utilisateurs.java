package Socket;

import java.util.HashMap;

public class Utilisateurs {

	private HashMap<String, String> listeUtilisateurs = new HashMap<>();

	public boolean authentification(String login, String password){

		addUtilisateurs();

		if (listeUtilisateurs.containsKey(login)){
			if (listeUtilisateurs.get(login).equals(password)){
				System.out.println("reussi");
				return true;
			}
			else
			{
				System.out.println("mauvais mdp");
				return false;
			}
		}
		else{
			System.out.println("login introuvable");
			return false;
		}

	}
	private void addUtilisateurs() {
		listeUtilisateurs.put("login1", "password1");
		listeUtilisateurs.put("login2", "password2");
		listeUtilisateurs.put("login3", "password3");
		listeUtilisateurs.put("login4", "password4");

	}
}
