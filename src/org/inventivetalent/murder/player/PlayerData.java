/*
 * Copyright 2013-2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.murder.player;

import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.murder.Murder;
import org.inventivetalent.murder.Role;
import org.inventivetalent.murder.game.Game;
import org.inventivetalent.murder.game.state.GameState;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerData extends StoredData {

	public UUID gameId;

	public String nameTag;
	public String disguiseTag;// Murderer only

	public GameState gameState = GameState.WAITING;
	public Role    role;
	public boolean isSpectator;

//	public int damageAmount;// Determines when the player is "dead"
	public boolean killed = false;
	public UUID killer;

	public int reloadTimer;// Delay until the gun is reloaded
	public int gunTimeout;// Delay for players who killed innocent bystanders
	public int knifeTimout;// Backup delay for the murderer to get their knife back
	public int speedTimeout;// Speed-Boost timeout

	public int lootCount;

	public BossBar bossBar;

	public PlayerData(UUID uuid) {
		super(uuid);
	}

	@Override
	public void storeData(boolean clear) {
		super.storeData(clear);
		Murder.instance.playerManager.saveDataToFile(this);
	}

	public boolean isInGame() {
		return gameId != null && getGame() != null;
	}

	@Nullable
	public Game getGame() {
		if (gameId == null) { return null; }
		return Murder.instance.gameManager.getGame(gameId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		if (!super.equals(o)) { return false; }

		PlayerData data = (PlayerData) o;

		if (nameTag != null ? !nameTag.equals(data.nameTag) : data.nameTag != null) { return false; }
		return gameState == data.gameState;

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (nameTag != null ? nameTag.hashCode() : 0);
		result = 31 * result + (gameState != null ? gameState.hashCode() : 0);
		return result;
	}
}
