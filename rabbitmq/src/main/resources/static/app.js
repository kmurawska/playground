/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/app.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./node_modules/node-libs-browser/mock/empty.js":
/*!******************************************************!*\
  !*** ./node_modules/node-libs-browser/mock/empty.js ***!
  \******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

eval("\n\n//# sourceURL=webpack:///./node_modules/node-libs-browser/mock/empty.js?");

/***/ }),

/***/ "./node_modules/stompjs/index.js":
/*!***************************************!*\
  !*** ./node_modules/stompjs/index.js ***!
  \***************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

eval("// Copyright (C) 2013 [Jeff Mesnil](http://jmesnil.net/)\n//\n//   Stomp Over WebSocket http://www.jmesnil.net/stomp-websocket/doc/ | Apache License V2.0\n//\n// The library can be used in node.js app to connect to STOMP brokers over TCP \n// or Web sockets.\n\n// Root of the `stompjs module`\n\nvar Stomp = __webpack_require__(/*! ./lib/stomp.js */ \"./node_modules/stompjs/lib/stomp.js\");\nvar StompNode = __webpack_require__(/*! ./lib/stomp-node.js */ \"./node_modules/stompjs/lib/stomp-node.js\");\n\nmodule.exports = Stomp.Stomp;\nmodule.exports.overTCP = StompNode.overTCP;\nmodule.exports.overWS = StompNode.overWS;\n\n//# sourceURL=webpack:///./node_modules/stompjs/index.js?");

/***/ }),

/***/ "./node_modules/stompjs/lib/stomp-node.js":
/*!************************************************!*\
  !*** ./node_modules/stompjs/lib/stomp-node.js ***!
  \************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

eval("// Generated by CoffeeScript 1.7.1\n\n/*\n   Stomp Over WebSocket http://www.jmesnil.net/stomp-websocket/doc/ | Apache License V2.0\n\n   Copyright (C) 2013 [Jeff Mesnil](http://jmesnil.net/)\n */\n\n(function() {\n  var Stomp, net, overTCP, overWS, wrapTCP, wrapWS;\n\n  Stomp = __webpack_require__(/*! ./stomp */ \"./node_modules/stompjs/lib/stomp.js\");\n\n  net = __webpack_require__(/*! net */ \"./node_modules/node-libs-browser/mock/empty.js\");\n\n  Stomp.Stomp.setInterval = function(interval, f) {\n    return setInterval(f, interval);\n  };\n\n  Stomp.Stomp.clearInterval = function(id) {\n    return clearInterval(id);\n  };\n\n  wrapTCP = function(port, host) {\n    var socket, ws;\n    socket = null;\n    ws = {\n      url: 'tcp:// ' + host + ':' + port,\n      send: function(d) {\n        return socket.write(d);\n      },\n      close: function() {\n        return socket.end();\n      }\n    };\n    socket = net.connect(port, host, function(e) {\n      return ws.onopen();\n    });\n    socket.on('error', function(e) {\n      return typeof ws.onclose === \"function\" ? ws.onclose(e) : void 0;\n    });\n    socket.on('close', function(e) {\n      return typeof ws.onclose === \"function\" ? ws.onclose(e) : void 0;\n    });\n    socket.on('data', function(data) {\n      var event;\n      event = {\n        'data': data.toString()\n      };\n      return ws.onmessage(event);\n    });\n    return ws;\n  };\n\n  wrapWS = function(url) {\n    var WebSocketClient, connection, socket, ws;\n    WebSocketClient = __webpack_require__(/*! websocket */ \"./node_modules/websocket/lib/browser.js\").client;\n    connection = null;\n    ws = {\n      url: url,\n      send: function(d) {\n        return connection.sendUTF(d);\n      },\n      close: function() {\n        return connection.close();\n      }\n    };\n    socket = new WebSocketClient();\n    socket.on('connect', function(conn) {\n      connection = conn;\n      ws.onopen();\n      connection.on('error', function(error) {\n        return typeof ws.onclose === \"function\" ? ws.onclose(error) : void 0;\n      });\n      connection.on('close', function() {\n        return typeof ws.onclose === \"function\" ? ws.onclose() : void 0;\n      });\n      return connection.on('message', function(message) {\n        var event;\n        if (message.type === 'utf8') {\n          event = {\n            'data': message.utf8Data\n          };\n          return ws.onmessage(event);\n        }\n      });\n    });\n    socket.connect(url);\n    return ws;\n  };\n\n  overTCP = function(host, port) {\n    var socket;\n    socket = wrapTCP(port, host);\n    return Stomp.Stomp.over(socket);\n  };\n\n  overWS = function(url) {\n    var socket;\n    socket = wrapWS(url);\n    return Stomp.Stomp.over(socket);\n  };\n\n  exports.overTCP = overTCP;\n\n  exports.overWS = overWS;\n\n}).call(this);\n\n\n//# sourceURL=webpack:///./node_modules/stompjs/lib/stomp-node.js?");

/***/ }),

/***/ "./node_modules/stompjs/lib/stomp.js":
/*!*******************************************!*\
  !*** ./node_modules/stompjs/lib/stomp.js ***!
  \*******************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

eval("// Generated by CoffeeScript 1.7.1\n\n/*\n   Stomp Over WebSocket http://www.jmesnil.net/stomp-websocket/doc/ | Apache License V2.0\n\n   Copyright (C) 2010-2013 [Jeff Mesnil](http://jmesnil.net/)\n   Copyright (C) 2012 [FuseSource, Inc.](http://fusesource.com)\n */\n\n(function() {\n  var Byte, Client, Frame, Stomp,\n    __hasProp = {}.hasOwnProperty,\n    __slice = [].slice;\n\n  Byte = {\n    LF: '\\x0A',\n    NULL: '\\x00'\n  };\n\n  Frame = (function() {\n    var unmarshallSingle;\n\n    function Frame(command, headers, body) {\n      this.command = command;\n      this.headers = headers != null ? headers : {};\n      this.body = body != null ? body : '';\n    }\n\n    Frame.prototype.toString = function() {\n      var lines, name, skipContentLength, value, _ref;\n      lines = [this.command];\n      skipContentLength = this.headers['content-length'] === false ? true : false;\n      if (skipContentLength) {\n        delete this.headers['content-length'];\n      }\n      _ref = this.headers;\n      for (name in _ref) {\n        if (!__hasProp.call(_ref, name)) continue;\n        value = _ref[name];\n        lines.push(\"\" + name + \":\" + value);\n      }\n      if (this.body && !skipContentLength) {\n        lines.push(\"content-length:\" + (Frame.sizeOfUTF8(this.body)));\n      }\n      lines.push(Byte.LF + this.body);\n      return lines.join(Byte.LF);\n    };\n\n    Frame.sizeOfUTF8 = function(s) {\n      if (s) {\n        return encodeURI(s).match(/%..|./g).length;\n      } else {\n        return 0;\n      }\n    };\n\n    unmarshallSingle = function(data) {\n      var body, chr, command, divider, headerLines, headers, i, idx, len, line, start, trim, _i, _j, _len, _ref, _ref1;\n      divider = data.search(RegExp(\"\" + Byte.LF + Byte.LF));\n      headerLines = data.substring(0, divider).split(Byte.LF);\n      command = headerLines.shift();\n      headers = {};\n      trim = function(str) {\n        return str.replace(/^\\s+|\\s+$/g, '');\n      };\n      _ref = headerLines.reverse();\n      for (_i = 0, _len = _ref.length; _i < _len; _i++) {\n        line = _ref[_i];\n        idx = line.indexOf(':');\n        headers[trim(line.substring(0, idx))] = trim(line.substring(idx + 1));\n      }\n      body = '';\n      start = divider + 2;\n      if (headers['content-length']) {\n        len = parseInt(headers['content-length']);\n        body = ('' + data).substring(start, start + len);\n      } else {\n        chr = null;\n        for (i = _j = start, _ref1 = data.length; start <= _ref1 ? _j < _ref1 : _j > _ref1; i = start <= _ref1 ? ++_j : --_j) {\n          chr = data.charAt(i);\n          if (chr === Byte.NULL) {\n            break;\n          }\n          body += chr;\n        }\n      }\n      return new Frame(command, headers, body);\n    };\n\n    Frame.unmarshall = function(datas) {\n      var data;\n      return (function() {\n        var _i, _len, _ref, _results;\n        _ref = datas.split(RegExp(\"\" + Byte.NULL + Byte.LF + \"*\"));\n        _results = [];\n        for (_i = 0, _len = _ref.length; _i < _len; _i++) {\n          data = _ref[_i];\n          if ((data != null ? data.length : void 0) > 0) {\n            _results.push(unmarshallSingle(data));\n          }\n        }\n        return _results;\n      })();\n    };\n\n    Frame.marshall = function(command, headers, body) {\n      var frame;\n      frame = new Frame(command, headers, body);\n      return frame.toString() + Byte.NULL;\n    };\n\n    return Frame;\n\n  })();\n\n  Client = (function() {\n    var now;\n\n    function Client(ws) {\n      this.ws = ws;\n      this.ws.binaryType = \"arraybuffer\";\n      this.counter = 0;\n      this.connected = false;\n      this.heartbeat = {\n        outgoing: 10000,\n        incoming: 10000\n      };\n      this.maxWebSocketFrameSize = 16 * 1024;\n      this.subscriptions = {};\n    }\n\n    Client.prototype.debug = function(message) {\n      var _ref;\n      return typeof window !== \"undefined\" && window !== null ? (_ref = window.console) != null ? _ref.log(message) : void 0 : void 0;\n    };\n\n    now = function() {\n      if (Date.now) {\n        return Date.now();\n      } else {\n        return new Date().valueOf;\n      }\n    };\n\n    Client.prototype._transmit = function(command, headers, body) {\n      var out;\n      out = Frame.marshall(command, headers, body);\n      if (typeof this.debug === \"function\") {\n        this.debug(\">>> \" + out);\n      }\n      while (true) {\n        if (out.length > this.maxWebSocketFrameSize) {\n          this.ws.send(out.substring(0, this.maxWebSocketFrameSize));\n          out = out.substring(this.maxWebSocketFrameSize);\n          if (typeof this.debug === \"function\") {\n            this.debug(\"remaining = \" + out.length);\n          }\n        } else {\n          return this.ws.send(out);\n        }\n      }\n    };\n\n    Client.prototype._setupHeartbeat = function(headers) {\n      var serverIncoming, serverOutgoing, ttl, v, _ref, _ref1;\n      if ((_ref = headers.version) !== Stomp.VERSIONS.V1_1 && _ref !== Stomp.VERSIONS.V1_2) {\n        return;\n      }\n      _ref1 = (function() {\n        var _i, _len, _ref1, _results;\n        _ref1 = headers['heart-beat'].split(\",\");\n        _results = [];\n        for (_i = 0, _len = _ref1.length; _i < _len; _i++) {\n          v = _ref1[_i];\n          _results.push(parseInt(v));\n        }\n        return _results;\n      })(), serverOutgoing = _ref1[0], serverIncoming = _ref1[1];\n      if (!(this.heartbeat.outgoing === 0 || serverIncoming === 0)) {\n        ttl = Math.max(this.heartbeat.outgoing, serverIncoming);\n        if (typeof this.debug === \"function\") {\n          this.debug(\"send PING every \" + ttl + \"ms\");\n        }\n        this.pinger = Stomp.setInterval(ttl, (function(_this) {\n          return function() {\n            _this.ws.send(Byte.LF);\n            return typeof _this.debug === \"function\" ? _this.debug(\">>> PING\") : void 0;\n          };\n        })(this));\n      }\n      if (!(this.heartbeat.incoming === 0 || serverOutgoing === 0)) {\n        ttl = Math.max(this.heartbeat.incoming, serverOutgoing);\n        if (typeof this.debug === \"function\") {\n          this.debug(\"check PONG every \" + ttl + \"ms\");\n        }\n        return this.ponger = Stomp.setInterval(ttl, (function(_this) {\n          return function() {\n            var delta;\n            delta = now() - _this.serverActivity;\n            if (delta > ttl * 2) {\n              if (typeof _this.debug === \"function\") {\n                _this.debug(\"did not receive server activity for the last \" + delta + \"ms\");\n              }\n              return _this.ws.close();\n            }\n          };\n        })(this));\n      }\n    };\n\n    Client.prototype._parseConnect = function() {\n      var args, connectCallback, errorCallback, headers;\n      args = 1 <= arguments.length ? __slice.call(arguments, 0) : [];\n      headers = {};\n      switch (args.length) {\n        case 2:\n          headers = args[0], connectCallback = args[1];\n          break;\n        case 3:\n          if (args[1] instanceof Function) {\n            headers = args[0], connectCallback = args[1], errorCallback = args[2];\n          } else {\n            headers.login = args[0], headers.passcode = args[1], connectCallback = args[2];\n          }\n          break;\n        case 4:\n          headers.login = args[0], headers.passcode = args[1], connectCallback = args[2], errorCallback = args[3];\n          break;\n        default:\n          headers.login = args[0], headers.passcode = args[1], connectCallback = args[2], errorCallback = args[3], headers.host = args[4];\n      }\n      return [headers, connectCallback, errorCallback];\n    };\n\n    Client.prototype.connect = function() {\n      var args, errorCallback, headers, out;\n      args = 1 <= arguments.length ? __slice.call(arguments, 0) : [];\n      out = this._parseConnect.apply(this, args);\n      headers = out[0], this.connectCallback = out[1], errorCallback = out[2];\n      if (typeof this.debug === \"function\") {\n        this.debug(\"Opening Web Socket...\");\n      }\n      this.ws.onmessage = (function(_this) {\n        return function(evt) {\n          var arr, c, client, data, frame, messageID, onreceive, subscription, _i, _len, _ref, _results;\n          data = typeof ArrayBuffer !== 'undefined' && evt.data instanceof ArrayBuffer ? (arr = new Uint8Array(evt.data), typeof _this.debug === \"function\" ? _this.debug(\"--- got data length: \" + arr.length) : void 0, ((function() {\n            var _i, _len, _results;\n            _results = [];\n            for (_i = 0, _len = arr.length; _i < _len; _i++) {\n              c = arr[_i];\n              _results.push(String.fromCharCode(c));\n            }\n            return _results;\n          })()).join('')) : evt.data;\n          _this.serverActivity = now();\n          if (data === Byte.LF) {\n            if (typeof _this.debug === \"function\") {\n              _this.debug(\"<<< PONG\");\n            }\n            return;\n          }\n          if (typeof _this.debug === \"function\") {\n            _this.debug(\"<<< \" + data);\n          }\n          _ref = Frame.unmarshall(data);\n          _results = [];\n          for (_i = 0, _len = _ref.length; _i < _len; _i++) {\n            frame = _ref[_i];\n            switch (frame.command) {\n              case \"CONNECTED\":\n                if (typeof _this.debug === \"function\") {\n                  _this.debug(\"connected to server \" + frame.headers.server);\n                }\n                _this.connected = true;\n                _this._setupHeartbeat(frame.headers);\n                _results.push(typeof _this.connectCallback === \"function\" ? _this.connectCallback(frame) : void 0);\n                break;\n              case \"MESSAGE\":\n                subscription = frame.headers.subscription;\n                onreceive = _this.subscriptions[subscription] || _this.onreceive;\n                if (onreceive) {\n                  client = _this;\n                  messageID = frame.headers[\"message-id\"];\n                  frame.ack = function(headers) {\n                    if (headers == null) {\n                      headers = {};\n                    }\n                    return client.ack(messageID, subscription, headers);\n                  };\n                  frame.nack = function(headers) {\n                    if (headers == null) {\n                      headers = {};\n                    }\n                    return client.nack(messageID, subscription, headers);\n                  };\n                  _results.push(onreceive(frame));\n                } else {\n                  _results.push(typeof _this.debug === \"function\" ? _this.debug(\"Unhandled received MESSAGE: \" + frame) : void 0);\n                }\n                break;\n              case \"RECEIPT\":\n                _results.push(typeof _this.onreceipt === \"function\" ? _this.onreceipt(frame) : void 0);\n                break;\n              case \"ERROR\":\n                _results.push(typeof errorCallback === \"function\" ? errorCallback(frame) : void 0);\n                break;\n              default:\n                _results.push(typeof _this.debug === \"function\" ? _this.debug(\"Unhandled frame: \" + frame) : void 0);\n            }\n          }\n          return _results;\n        };\n      })(this);\n      this.ws.onclose = (function(_this) {\n        return function() {\n          var msg;\n          msg = \"Whoops! Lost connection to \" + _this.ws.url;\n          if (typeof _this.debug === \"function\") {\n            _this.debug(msg);\n          }\n          _this._cleanUp();\n          return typeof errorCallback === \"function\" ? errorCallback(msg) : void 0;\n        };\n      })(this);\n      return this.ws.onopen = (function(_this) {\n        return function() {\n          if (typeof _this.debug === \"function\") {\n            _this.debug('Web Socket Opened...');\n          }\n          headers[\"accept-version\"] = Stomp.VERSIONS.supportedVersions();\n          headers[\"heart-beat\"] = [_this.heartbeat.outgoing, _this.heartbeat.incoming].join(',');\n          return _this._transmit(\"CONNECT\", headers);\n        };\n      })(this);\n    };\n\n    Client.prototype.disconnect = function(disconnectCallback, headers) {\n      if (headers == null) {\n        headers = {};\n      }\n      this._transmit(\"DISCONNECT\", headers);\n      this.ws.onclose = null;\n      this.ws.close();\n      this._cleanUp();\n      return typeof disconnectCallback === \"function\" ? disconnectCallback() : void 0;\n    };\n\n    Client.prototype._cleanUp = function() {\n      this.connected = false;\n      if (this.pinger) {\n        Stomp.clearInterval(this.pinger);\n      }\n      if (this.ponger) {\n        return Stomp.clearInterval(this.ponger);\n      }\n    };\n\n    Client.prototype.send = function(destination, headers, body) {\n      if (headers == null) {\n        headers = {};\n      }\n      if (body == null) {\n        body = '';\n      }\n      headers.destination = destination;\n      return this._transmit(\"SEND\", headers, body);\n    };\n\n    Client.prototype.subscribe = function(destination, callback, headers) {\n      var client;\n      if (headers == null) {\n        headers = {};\n      }\n      if (!headers.id) {\n        headers.id = \"sub-\" + this.counter++;\n      }\n      headers.destination = destination;\n      this.subscriptions[headers.id] = callback;\n      this._transmit(\"SUBSCRIBE\", headers);\n      client = this;\n      return {\n        id: headers.id,\n        unsubscribe: function() {\n          return client.unsubscribe(headers.id);\n        }\n      };\n    };\n\n    Client.prototype.unsubscribe = function(id) {\n      delete this.subscriptions[id];\n      return this._transmit(\"UNSUBSCRIBE\", {\n        id: id\n      });\n    };\n\n    Client.prototype.begin = function(transaction) {\n      var client, txid;\n      txid = transaction || \"tx-\" + this.counter++;\n      this._transmit(\"BEGIN\", {\n        transaction: txid\n      });\n      client = this;\n      return {\n        id: txid,\n        commit: function() {\n          return client.commit(txid);\n        },\n        abort: function() {\n          return client.abort(txid);\n        }\n      };\n    };\n\n    Client.prototype.commit = function(transaction) {\n      return this._transmit(\"COMMIT\", {\n        transaction: transaction\n      });\n    };\n\n    Client.prototype.abort = function(transaction) {\n      return this._transmit(\"ABORT\", {\n        transaction: transaction\n      });\n    };\n\n    Client.prototype.ack = function(messageID, subscription, headers) {\n      if (headers == null) {\n        headers = {};\n      }\n      headers[\"message-id\"] = messageID;\n      headers.subscription = subscription;\n      return this._transmit(\"ACK\", headers);\n    };\n\n    Client.prototype.nack = function(messageID, subscription, headers) {\n      if (headers == null) {\n        headers = {};\n      }\n      headers[\"message-id\"] = messageID;\n      headers.subscription = subscription;\n      return this._transmit(\"NACK\", headers);\n    };\n\n    return Client;\n\n  })();\n\n  Stomp = {\n    VERSIONS: {\n      V1_0: '1.0',\n      V1_1: '1.1',\n      V1_2: '1.2',\n      supportedVersions: function() {\n        return '1.1,1.0';\n      }\n    },\n    client: function(url, protocols) {\n      var klass, ws;\n      if (protocols == null) {\n        protocols = ['v10.stomp', 'v11.stomp'];\n      }\n      klass = Stomp.WebSocketClass || WebSocket;\n      ws = new klass(url, protocols);\n      return new Client(ws);\n    },\n    over: function(ws) {\n      return new Client(ws);\n    },\n    Frame: Frame\n  };\n\n  if ( true && exports !== null) {\n    exports.Stomp = Stomp;\n  }\n\n  if (typeof window !== \"undefined\" && window !== null) {\n    Stomp.setInterval = function(interval, f) {\n      return window.setInterval(f, interval);\n    };\n    Stomp.clearInterval = function(id) {\n      return window.clearInterval(id);\n    };\n    window.Stomp = Stomp;\n  } else if (!exports) {\n    self.Stomp = Stomp;\n  }\n\n}).call(this);\n\n\n//# sourceURL=webpack:///./node_modules/stompjs/lib/stomp.js?");

/***/ }),

/***/ "./node_modules/websocket/lib/browser.js":
/*!***********************************************!*\
  !*** ./node_modules/websocket/lib/browser.js ***!
  \***********************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

eval("var _global = (function() { return this; })();\nvar NativeWebSocket = _global.WebSocket || _global.MozWebSocket;\nvar websocket_version = __webpack_require__(/*! ./version */ \"./node_modules/websocket/lib/version.js\");\n\n\n/**\n * Expose a W3C WebSocket class with just one or two arguments.\n */\nfunction W3CWebSocket(uri, protocols) {\n\tvar native_instance;\n\n\tif (protocols) {\n\t\tnative_instance = new NativeWebSocket(uri, protocols);\n\t}\n\telse {\n\t\tnative_instance = new NativeWebSocket(uri);\n\t}\n\n\t/**\n\t * 'native_instance' is an instance of nativeWebSocket (the browser's WebSocket\n\t * class). Since it is an Object it will be returned as it is when creating an\n\t * instance of W3CWebSocket via 'new W3CWebSocket()'.\n\t *\n\t * ECMAScript 5: http://bclary.com/2004/11/07/#a-13.2.2\n\t */\n\treturn native_instance;\n}\nif (NativeWebSocket) {\n\t['CONNECTING', 'OPEN', 'CLOSING', 'CLOSED'].forEach(function(prop) {\n\t\tObject.defineProperty(W3CWebSocket, prop, {\n\t\t\tget: function() { return NativeWebSocket[prop]; }\n\t\t});\n\t});\n}\n\n/**\n * Module exports.\n */\nmodule.exports = {\n    'w3cwebsocket' : NativeWebSocket ? W3CWebSocket : null,\n    'version'      : websocket_version\n};\n\n\n//# sourceURL=webpack:///./node_modules/websocket/lib/browser.js?");

/***/ }),

/***/ "./node_modules/websocket/lib/version.js":
/*!***********************************************!*\
  !*** ./node_modules/websocket/lib/version.js ***!
  \***********************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

eval("module.exports = __webpack_require__(/*! ../package.json */ \"./node_modules/websocket/package.json\").version;\n\n\n//# sourceURL=webpack:///./node_modules/websocket/lib/version.js?");

/***/ }),

/***/ "./node_modules/websocket/package.json":
/*!*********************************************!*\
  !*** ./node_modules/websocket/package.json ***!
  \*********************************************/
/*! exports provided: _from, _id, _inBundle, _integrity, _location, _phantomChildren, _requested, _requiredBy, _resolved, _shasum, _spec, _where, author, browser, bugs, bundleDependencies, config, contributors, dependencies, deprecated, description, devDependencies, directories, engines, homepage, keywords, license, main, name, repository, scripts, version, default */
/***/ (function(module) {

eval("module.exports = {\"_from\":\"websocket@latest\",\"_id\":\"websocket@1.0.28\",\"_inBundle\":false,\"_integrity\":\"sha512-00y/20/80P7H4bCYkzuuvvfDvh+dgtXi5kzDf3UcZwN6boTYaKvsrtZ5lIYm1Gsg48siMErd9M4zjSYfYFHTrA==\",\"_location\":\"/websocket\",\"_phantomChildren\":{},\"_requested\":{\"type\":\"tag\",\"registry\":true,\"raw\":\"websocket@latest\",\"name\":\"websocket\",\"escapedName\":\"websocket\",\"rawSpec\":\"latest\",\"saveSpec\":null,\"fetchSpec\":\"latest\"},\"_requiredBy\":[\"/stompjs\"],\"_resolved\":\"https://registry.npmjs.org/websocket/-/websocket-1.0.28.tgz\",\"_shasum\":\"9e5f6fdc8a3fe01d4422647ef93abdd8d45a78d3\",\"_spec\":\"websocket@latest\",\"_where\":\"D:\\\\IT\\\\workspace\\\\playground\\\\rabbitmq\\\\ui\\\\node_modules\\\\stompjs\",\"author\":{\"name\":\"Brian McKelvey\",\"email\":\"theturtle32@gmail.com\",\"url\":\"https://github.com/theturtle32\"},\"browser\":\"lib/browser.js\",\"bugs\":{\"url\":\"https://github.com/theturtle32/WebSocket-Node/issues\"},\"bundleDependencies\":false,\"config\":{\"verbose\":false},\"contributors\":[{\"name\":\"Iñaki Baz Castillo\",\"email\":\"ibc@aliax.net\",\"url\":\"http://dev.sipdoc.net\"}],\"dependencies\":{\"debug\":\"^2.2.0\",\"nan\":\"^2.11.0\",\"typedarray-to-buffer\":\"^3.1.5\",\"yaeti\":\"^0.0.6\"},\"deprecated\":false,\"description\":\"Websocket Client & Server Library implementing the WebSocket protocol as specified in RFC 6455.\",\"devDependencies\":{\"buffer-equal\":\"^1.0.0\",\"faucet\":\"^0.0.1\",\"gulp\":\"git+https://github.com/gulpjs/gulp.git#4.0\",\"gulp-jshint\":\"^2.0.4\",\"jshint\":\"^2.0.0\",\"jshint-stylish\":\"^2.2.1\",\"tape\":\"^4.9.1\"},\"directories\":{\"lib\":\"./lib\"},\"engines\":{\"node\":\">=0.10.0\"},\"homepage\":\"https://github.com/theturtle32/WebSocket-Node\",\"keywords\":[\"websocket\",\"websockets\",\"socket\",\"networking\",\"comet\",\"push\",\"RFC-6455\",\"realtime\",\"server\",\"client\"],\"license\":\"Apache-2.0\",\"main\":\"index\",\"name\":\"websocket\",\"repository\":{\"type\":\"git\",\"url\":\"git+https://github.com/theturtle32/WebSocket-Node.git\"},\"scripts\":{\"gulp\":\"gulp\",\"install\":\"(node-gyp rebuild 2> builderror.log) || (exit 0)\",\"test\":\"faucet test/unit\"},\"version\":\"1.0.28\"};\n\n//# sourceURL=webpack:///./node_modules/websocket/package.json?");

/***/ }),

/***/ "./src/app.js":
/*!********************!*\
  !*** ./src/app.js ***!
  \********************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

eval("var Stomp = __webpack_require__(/*! stompjs */ \"./node_modules/stompjs/index.js\");\n\nfunction subscribeToEvents() {\n  var ws = new WebSocket('ws://localhost:15674/ws');\n  var client = Stomp.over(ws);\n  client.connect('guest', 'guest', function () {\n    return onConnect(client);\n  }, onError, '/');\n}\n\nfunction onConnect(client) {\n  console.log(\"connected\");\n  console.log(event);\n  subscribe(client);\n}\n\nfunction subscribe(client) {\n  //  subscribeToNamedQueue(client);\n  subscribeToFanoutExchange(client);\n  subscribeToDirectExchange(client);\n  subscribeToTopicExchange(client);\n}\n\nfunction subscribeToNamedQueue(client) {\n  client.subscribe(\"/queue/QUEUE_2\", handle);\n}\n\nfunction subscribeToFanoutExchange(client) {\n  client.subscribe(\"/exchange/FANOUT_EXCHANGE\", handle);\n}\n\nfunction subscribeToDirectExchange(client) {\n  var routingKey = \"BLACK\";\n  client.subscribe(\"/exchange/DIRECT_EXCHANGE/\" + routingKey, handle);\n}\n\nfunction subscribeToTopicExchange(client) {\n  var routingKey = \"lazy.*.*\";\n  client.subscribe(\"/exchange/TOPIC_EXCHANGE/\" + routingKey, handle);\n}\n\nfunction onError() {\n  console.log('error');\n}\n\nfunction handle(event) {\n  console.log(event);\n}\n\nsubscribeToEvents();\n\n//# sourceURL=webpack:///./src/app.js?");

/***/ })

/******/ });