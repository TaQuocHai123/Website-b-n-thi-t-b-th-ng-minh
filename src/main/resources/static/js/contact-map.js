function initMap() {
  // Shop address provided by the user. We'll geocode it when possible.
  const storeAddress = '364/45/27 Dương Quảng Hàm, Phường 5, Gò Vấp, Hồ Chí Minh, Việt Nam';
  const encodedAddress = encodeURIComponent(storeAddress);

  // Fallback coordinates (Ho Chi Minh City center) until geocoding completes
  let storeLatLng = { lat: 10.8231, lng: 106.6297 };

  const map = new google.maps.Map(document.getElementById('google_map'), {
    center: storeLatLng,
    zoom: 14,
    mapTypeControl: false
  });

  const marker = new google.maps.Marker({
    position: storeLatLng,
    map: map,
    title: 'Cửa hàng của chúng tôi'
  });

  // Try to geocode the textual address (requires Google Maps JS API & a valid key)
  if (window.google && google.maps && google.maps.Geocoder) {
    try {
      const geocoder = new google.maps.Geocoder();
      geocoder.geocode({ address: storeAddress }, function (results, status) {
        if (status === 'OK' && results && results[0]) {
          const loc = results[0].geometry.location.toJSON();
          storeLatLng = { lat: loc.lat, lng: loc.lng };
          map.setCenter(storeLatLng);
          marker.setPosition(storeLatLng);
        }
      });
    } catch (e) {
      console.warn('Geocode failed:', e);
    }
  }

  // InfoWindow content (open by default to match the example)
  const infoContent = `
    <div style="max-width:280px;font-family:Arial,Helvetica,sans-serif;font-size:13px;">
      <strong>HỘ KINH DOANH THIẾT BỊ ĐIỆN THÔNG MINH</strong>
      <div style="margin-top:6px;">${storeAddress}</div>
      <div style="margin-top:8px;"><a target="_blank" href="https://www.google.com/maps/dir/?api=1&destination=${encodedAddress}" style="color:#1a73e8;text-decoration:none;font-weight:600;">Chỉ đường</a></div>
      <div style="margin-top:6px;"><a target="_blank" href="https://www.google.com/maps/search/?api=1&query=${encodedAddress}" style="color:#1a73e8;text-decoration:none;">Xem bản đồ lớn hơn</a></div>
    </div>
  `;

  const infoWindow = new google.maps.InfoWindow({
    content: infoContent,
    maxWidth: 280
  });

  // Open info window by default
  infoWindow.open({ map, anchor: marker });

  // Reopen on marker click
  marker.addListener('click', function() {
    infoWindow.open({ map, anchor: marker });
  });

  const directionsService = new google.maps.DirectionsService();
  const directionsRenderer = new google.maps.DirectionsRenderer({
    map: map,
    panel: document.getElementById('directions-panel')
  });

  document.getElementById('btn-route').addEventListener('click', function () {
    const startVal = document.getElementById('start-input').value.trim();
    if (!startVal) {
      // Try geolocation
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (pos) {
          const userLatLng = { lat: pos.coords.latitude, lng: pos.coords.longitude };
          calcRoute(userLatLng, storeLatLng);
        }, function (err) {
          alert('Không thể lấy vị trí của bạn: ' + err.message);
        });
      } else {
        alert('Trình duyệt không hỗ trợ geolocation. Vui lòng nhập địa chỉ bắt đầu.');
      }
    } else {
      calcRoute(startVal, storeLatLng);
    }
  });

  document.getElementById('btn-clear').addEventListener('click', function () {
    directionsRenderer.set('directions', null);
    document.getElementById('start-input').value = '';
    document.getElementById('directions-panel').innerHTML = '';
    map.setCenter(storeLatLng);
    map.setZoom(14);
  });

  function calcRoute(origin, destination) {
    const req = {
      origin: origin,
      destination: destination,
      travelMode: google.maps.TravelMode.DRIVING
    };
    directionsService.route(req, function (result, status) {
      if (status === 'OK') {
        directionsRenderer.setDirections(result);
      } else {
        alert('Không tìm được đường đi: ' + status);
      }
    });
  }
}

// Expose initMap to the window so Google Maps callback can call it
window.initMap = initMap;

// Fallback: if Google Maps never loads or initMap never runs, show an error message
(function() {
  var mapLoadTimeout = setTimeout(function() {
    if (!window.google || !window.google.maps) {
      var el = document.getElementById('google_map');
      if (el) {
        el.innerHTML = '<div style="padding:24px;color:#333;background:#fff;border:1px solid #eee;border-radius:6px;">\
          <strong>Không thể tải bản đồ</strong><br/>\
          Vui lòng kiểm tra API key Google Maps, kết nối mạng và console của trình duyệt (F12).\
          </div>';
      }
    }
  }, 7000);

  // If initMap runs it should clear the timeout; wrap original initMap to clear
  var originalInit = window.initMap;
  window.initMap = function() {
    clearTimeout(mapLoadTimeout);
    if (typeof originalInit === 'function') originalInit();
  };
})();
