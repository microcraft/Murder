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

package org.inventivetalent.murder.game.state.executor.ingame;

import com.google.common.base.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.inventivetalent.murder.Role;
import org.inventivetalent.murder.game.Game;
import org.inventivetalent.murder.game.state.GameState;
import org.inventivetalent.murder.player.PlayerData;

import java.util.UUID;

public class StartedExecutor extends IngameExecutor {

	boolean firstTick = true;

	public StartedExecutor(Game game) {
		super(game);
	}

	@Override
	public void tick() {
		super.tick();

		if (firstTick) {
			firstTick = false;

			updatePlayerStates(GameState.STARTED, new Predicate<PlayerData>() {
				@Override
				public boolean apply(PlayerData playerData) {
					Player player = playerData.getPlayer();

					//Remove black screen
					player.getInventory().setHelmet(null);

					//Clear effects
					for (PotionEffect effect : player.getActivePotionEffects()) {
						player.removePotionEffect(effect.getType());
					}
					for (UUID uuid1 : game.players) {
						Player player1 = game.getPlayer(uuid1);
						if (player1 != null) { player1.showPlayer(playerData.getPlayer()); }
					}

					player.setWalkSpeed(0.2f);
					if (playerData.role != Role.MURDERER) {
						player.setFoodLevel(6);
					}
					return true;
				}
			});
		}
	}

	@Override
	public boolean finished() {
		return super.finished() || !firstTick;
	}
}
