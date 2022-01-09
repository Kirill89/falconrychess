$(function() {
  'use strict';

  var nextUrl;
  var timerId;
  var canceled;

  $('.new-game-friend-play').click(function () {
    var side = $(this).data('side');

    $.rpc({ action: $.rpc.actions.newGame }, function (gameId) {
      $('.new-game-invite-dlg').on('shown.bs.modal', function () {
        $('.new-game-invite-dlg').off('shown.bs.modal');

        $('.new-game-invite-dlg__url').val($.gameUrl + '?id=' + gameId + '' + (side === 0 ? '1' : '0')).focus().get(0).select();
        nextUrl = $.gameUrl + '?id=' + gameId + '' + side;
      }).modal('show');
    });
  });

  $('.new-game-invite-start').click(function () {
    window.location.href = nextUrl;
  });

  function findRndGame(id) {
    clearTimeout(timerId);

    if (canceled) {
      return;
    }

    if (!id) {
      id = 0;
    }

    $.ajax({
      url: $.server + 'rnd',
      dataType: 'jsonp',
      jsonp: 'callback',
      data: {
        id: id
      }
    }).done(function(data) {
      if (canceled) {
        return;
      }

      if (data.gameId) {
        window.location.href = $.gameUrl + '?id=' + data.gameId;
        return;
      }

      timerId = setTimeout(function () {
        findRndGame(data.id);
      }, 5000);
    });
  }

  $('.new-game-rand-play').click(function () {
    canceled = false;
    findRndGame();

    $('.new-game-rand-dlg').on('hide.bs.modal', function () {
      clearTimeout(timerId);
      canceled = true;
      $('.new-game-rand-dlg').off('hide.bs.modal');
    }).modal('show');
  });
});
