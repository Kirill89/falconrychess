$(function($) {
  'use strict';

  /////////////////////////////////////////////////////////////////////////////
  // Global
  $.gameUrl = window.location.origin + '/game.html';
  $.server = '/';


  /////////////////////////////////////////////////////////////////////////////
  // RPC
  var API = $.server + 'game';
  var defaults = { gameId: '', action: '', figure: '', x0: -1, y0: -1, x1: -1, y1: -1 };

  var started = false;
  var errored = false;

  var messages = {
    kingIsUnderAttackAfterMove: 'You can\'t make this move. King will be under attack.',
    gameIsOver: 'Sorry, game is already over.'
  };

  /*
   * command:
   * - gameId
   * - action - newGame | getMoveArea | getStatus | replace | move | getField | getGameLog
   * - figure - Queen | Horse | Rook | Elephant | Falcon | Dolphin
   * - x0
   * - y0
   * - x1
   * - y1
   */
  $.rpc = function(command, callback) {
    if (started) {
      console.log('RPC: request already started');
      return;
    }

    started = true;

    $.ajax({
      url: API,
      dataType: 'jsonp',
      jsonp: 'callback',
      data: {
        command: JSON.stringify($.extend({}, defaults, command))
      }
    })
      .done(function (data) {
        if (data.status === 'ok') {
          var result;

          try {
            result = JSON.parse(data.message);
          } catch (e) {
            result = data.message;
          }

          started = false;
          errored = false;
          callback(result);
        } else {
          if (messages[data.message]) {
            alert(messages[data.message]);
          } else {
            alert(data.message);
          }
        }
      })
      .fail(function () {
        if (!errored) {
          alert('Can\'t make request. Please try again later.');
        }
        errored = true;
      })
      .always(function () {
        started = false;
      });
  };

  $.rpc.actions = {
    newGame: 'newGame',
    getMoveArea: 'getMoveArea',
    getStatus: 'getStatus',
    replace: 'replace',
    move: 'move',
    getField: 'getField',
    getGameLog: 'getGameLog'
  };

}(jQuery));
