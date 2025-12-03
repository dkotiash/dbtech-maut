package de.htwberlin.dbtech.aufgaben.ue03.mauterhebung;

import de.htwberlin.dbtech.aufgaben.ue03.dao.MautDao;
import de.htwberlin.dbtech.aufgaben.ue03.dao.MautDaoImpl;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugData;
import java.sql.Connection;

public class MautServiceImpl implements IMautService {

	private MautDao mautDao;

	@Override
	public void setConnection(Connection connection) {
		this.mautDao = new MautDaoImpl(connection);
	}

	@Override
	public void berechneMaut(int mautAbschnittId, int achsZahl, String kennzeichen) {
		FahrzeugData fzg = mautDao.findFahrzeugData(kennzeichen);
		MautLogik prozessor = new MautLogik(mautDao);

		if (fzg != null) {
			prozessor.verarbeiteAutomatischesVerfahren(fzg, mautAbschnittId, achsZahl);
		} else {
			prozessor.verarbeiteManuellesVerfahren(kennzeichen, mautAbschnittId, achsZahl);
		}
	}
}