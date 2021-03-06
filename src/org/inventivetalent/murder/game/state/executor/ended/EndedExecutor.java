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

package org.inventivetalent.murder.game.state.executor.ended;

import org.inventivetalent.murder.Murder;
import org.inventivetalent.murder.Role;
import org.inventivetalent.murder.game.Game;
import org.inventivetalent.murder.game.state.GameState;
import org.inventivetalent.murder.game.state.executor.LeavableExecutor;
import org.inventivetalent.murder.player.PlayerData;
import org.inventivetalent.pluginannotations.PluginAnnotations;
import org.inventivetalent.pluginannotations.message.MessageFormatter;
import org.inventivetalent.pluginannotations.message.MessageLoader;

public class EndedExecutor extends LeavableExecutor {

	static MessageLoader MESSAGE_LOADER = PluginAnnotations.MESSAGE.newMessageLoader(Murder.instance, "config.yml", "messages.game", null);

	boolean firstTick  = true;
	int     endSeconds = 0;

	public EndedExecutor(Game game) {
		super(game);
		game.ticks = 0;
	}

	@Override
	public void tick() {
		super.tick();

		if (firstTick) {
			firstTick = false;
			updatePlayerStates(GameState.ENDED, null);

			MessageFormatter murdererFormatter = new MessageFormatter() {
				@Override
				public String format(String key, String message) {
					PlayerData murdererData = Murder.instance.playerManager.getData(game.getMurderer());
					if (murdererData != null) {
						return String.format(message, murdererData.nameTag.substring(0, 2) + murdererData.getPlayer().getName(), murdererData.nameTag);
					}
					return String.format(message, "?", "?");
				}
			};

			if (game.winner == null) {
				game.broadcastMessage(MESSAGE_LOADER.getMessage("winner.draw", "winner.draw"));
			} else if (game.winner == Role.WEAPON) {
				game.broadcastMessage(MESSAGE_LOADER.getMessage("winner.bystander", "winner.bystander", murdererFormatter));
			} else if (game.winner == Role.MURDERER) {
				game.broadcastMessage(MESSAGE_LOADER.getMessage("winner.murderer", "winner.murderer", murdererFormatter));
			} else {
				Murder.instance.getLogger().warning("Invalid Winner: " + game.winner);
			}
			//TODO: print scoreboard
		}

		game.ticks++;
		if (game.ticks >= 20) {
			endSeconds++;
			game.ticks = 0;
		}

	}

	@Override
	public boolean finished() {
		return super.finished() || endSeconds >= Murder.instance.endDelay;
	}
}
