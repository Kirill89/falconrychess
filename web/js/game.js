// TODO:
// - replace

$(function() {
  'use strict';

  var gameId = window.location.search.split('=')[1];
  var mySide = gameId.substring(gameId.length - 1) === '0' ? 'WHITE' : 'BLACK';
  gameId = gameId.substring(0, gameId.length - 1);
  var st;
  var updateTimer = -1;
  var $gameStatus = $('.game-status');
  var $gameBeep = $('.game-beep');
  var $gameLog = $('.game-log__content');

  var statusText = {
    you: {
      CHECKMATE: 'Checkmate! You loose.',
      CHECK: 'Check! Your turn.',
      JUJUBE: 'Jujube. You loose.',
      DRAW: 'Draw.',
      NONE: 'Your turn. Opponent waits.'
    },
    enemy: {
      CHECKMATE: 'Checkmate! You WIN!',
      CHECK: 'Check! Opponent turn. Please wait.',
      JUJUBE: 'Jujube. You WIN!',
      DRAW: 'Draw.',
      NONE: 'Opponent turn. Please wait.'
    }
  };

  if (mySide === 'BLACK') {
    $('.game__horizontal-bar, .game__vertical-bar').each(function () {
      var bar = $(this);
      var items = bar.children('div');
      bar.append(items.get().reverse());
    });
  }

  function getCellByXY(x, y) {
    if (mySide === 'BLACK') {
      x = 9 - x;
      y = 9 - y;
    }
    return $('.game-field__cell:eq(' + (y * 10 + x) + ')');
  }

  /////////////////////////////////////////////////////////////////////////////
  // Redraw field and log
  function redrawField(callback) {
    var obj;

    $.rpc({ gameId: gameId, action: $.rpc.actions.getGameLog }, function (log) {
      $gameLog.empty();

      var $row;

      for (var i = 0; i < log.length; i += 2) {
        $row = $('<div class="clearfix">');

        $row.append('<div class="game-log__num">' + (i / 2 + 1) + '</div>');

        $row.append('<div class="game-log__item">' + log[i] + '</div>');

        if (log[i + 1]) {
          $row.append('<div class="game-log__item">' + log[i + 1] + '</div>');
        }

        $gameLog.prepend($row);
      }

      $.rpc({ gameId: gameId, action: $.rpc.actions.getField }, function (data) {
        $('.game-field__cell')
          .css('background-image', 'none')
          .removeClass('game-field__cell__m-selected')
          .removeClass('game-field__cell__m-figure');

        for (var i = 0; i < 99; i++) {
          $('.game-field__cell:eq(' + i + ')').css('background-image', 'none');
        }

        for (var i = 0; i < data.length; i++) {
          obj = data[i];

          getCellByXY(obj.x, obj.y)
            .css('background-image', 'url(img/' + obj.side + '/' + obj.name + '.png)')
            .data('fig-side', obj.side)
            .data('fig-name', obj.name)
            .addClass('game-field__cell__m-figure');
        }

        callback();
      });

    });
  }

  /////////////////////////////////////////////////////////////////////////////
  // Update status, start timer
  function updateStatus() {
    $.rpc({ gameId: gameId, action: $.rpc.actions.getStatus }, function (data) {
      if (st && (st.side === data.side && st.status === data.status)) {
        return;
      }

      st = data;

      redrawField(function () {
        if (data.side === mySide) {
          $gameStatus.text(statusText.you[data.status]).addClass('bg-warning').removeClass('bg-info');

          clearInterval(updateTimer);
          try {
            $gameBeep.get(0).play();
          } catch (__) {
            // Skip for old browsers
          }
        } else {
          $gameStatus.text(statusText.enemy[data.status]).addClass('bg-info').removeClass('bg-warning');

          updateTimer = setInterval(updateStatus, 3000);
        }

        $gameStatus.append('<img class="game-status__side-logo" src="img/' + mySide + '/King.png">');
      });
    });
  }


  /////////////////////////////////////////////////////////////////////////////
  // Make move
  function makeMove(x0, y0, x1, y1) {
    $.rpc({ gameId: gameId, action: $.rpc.actions.move, x0: x0, y0: y0, x1: x1, y1: y1 }, function () {
      updateStatus();
    });
  }

  /////////////////////////////////////////////////////////////////////////////
  // Field cell click handler

  var x0, y0, name, x1, y1;

  $('.game-field__cell').click(function () {
    if (st.side !== mySide) {
      return;
    }

    var $this = $(this);
    var index = $this.index();
    var y = parseInt(index / 10);
    var x = index - y * 10;

    if (mySide === 'BLACK') {
      x = 9 - x;
      y = 9 - y;
    }

    if ($this.hasClass('game-field__cell__m-selected')) {
      // TODO: replace

      if ((mySide === 'WHITE' && y === 0) || (mySide === 'BLACK' && y === 9)) {
        x1 = x;
        y1 = y;

        if (name === 'Pawn') {
          $('.select-replace-pawn-dlg').modal('show');
          return;
        } else if (name === 'Prince') {
          $('.select-replace-prince-dlg').modal('show');
          return;
        }
      }

      makeMove(x0, y0, x, y);

      return;
    }

    if ($this.hasClass('game-field__cell__m-figure')) {
      if ($this.data('fig-side') !== mySide) {
        return;
      }

      $.rpc({ gameId: gameId, action: $.rpc.actions.getMoveArea, x0: x, y0: y }, function (data) {
        var pt;

        $('.game-field__cell').removeClass('game-field__cell__m-selected');
        for (var i = 0; i < data.length; i++) {
          pt = data[i];
          getCellByXY(pt.x, pt.y).addClass('game-field__cell__m-selected');
        }

        x0 = x;
        y0 = y;
        name = $this.data('fig-name');
      });

      return;
    }
  });

  $('.select-replace-pawn-dlg__button, .select-replace-prince-dlg__button').click(function () {
    $('.select-replace-pawn-dlg').modal('hide');
    $('.select-replace-prince-dlg').modal('hide');

    $.rpc({ gameId: gameId, action: $.rpc.actions.replace, figure: $(this).data('fig-name') }, function () {
      makeMove(x0, y0, x1, y1);
    });

    return false;
  });

  /////////////////////////////////////////////////////////////////////////////
  // Initial status update
  updateStatus();
});
