// NASA API base URL
const API_BASE = '/api/space';

// Initialize on page load
document.addEventListener('DOMContentLoaded', () => {
    loadApod();
    loadMarsRover();
    loadNeo();

    // Event listeners for filter buttons
    document.getElementById('marsBtn')?.addEventListener('click', loadMarsRover);
    document.getElementById('neoBtn')?.addEventListener('click', loadNeo);
});

// ==================== APOD ====================

async function loadApod() {
    const card = document.getElementById('apodCard');
    try {
        const response = await fetch(`${API_BASE}/apod`);
        const data = await response.json();

        if (data.error) {
            card.innerHTML = `<div class="error-message"><i class="fas fa-exclamation-circle"></i> ${data.message}</div>`;
            return;
        }

        card.innerHTML = `
            ${data.url ? `<img src="${data.url}" alt="${data.title || 'APOD'}" class="card-image" />` : ''}
            <div class="card-content">
                <h3 class="card-title">${data.title || 'Astronomy Picture of the Day'}</h3>
                <div class="card-meta">
                    <span class="meta-item"><i class="far fa-calendar"></i> ${data.date || 'Unknown date'}</span>
                    <span class="meta-item"><i class="fas fa-user"></i> ${data.copyright ? data.copyright : 'NASA'}</span>
                </div>
                <p class="card-description">${data.explanation || 'No description available.'}</p>
            </div>
        `;
    } catch (error) {
        card.innerHTML = `<div class="error-message"><i class="fas fa-exclamation-circle"></i> Unable to load APOD data. Please try again.</div>`;
        console.error('APOD Error:', error);
    }
}

// ==================== MARS ROVER ====================

async function loadMarsRover() {
    const sol = document.getElementById('solInput')?.value || '1000';
    const camera = document.getElementById('cameraSelect')?.value || 'fhaz';
    const grid = document.getElementById('marsGrid');

    // Show loading state
    grid.innerHTML = `
        <div class="card" style="grid-column: 1/-1;">
            <div class="loader">
                <div class="spinner"></div>
                <p>Loading Mars rover data for sol ${sol}...</p>
            </div>
        </div>
    `;

    try {
        const response = await fetch(`${API_BASE}/mars-rover?sol=${sol}&camera=${camera}`);
        const data = await response.json();

        if (data.error) {
            grid.innerHTML = `<div class="card" style="grid-column: 1/-1;"><div class="error-message"><i class="fas fa-exclamation-circle"></i> ${data.message}</div></div>`;
            return;
        }

        const photos = data.photos || [];
        if (photos.length === 0) {
            grid.innerHTML = `<div class="card" style="grid-column: 1/-1;"><div class="error-message"><i class="fas fa-info-circle"></i> No photos found for this sol and camera combination.</div></div>`;
            return;
        }

        grid.innerHTML = photos.map(photo => `
            <div class="card">
                <img src="${photo.img_src}" alt="Mars rover photo" class="card-image" />
                <div class="card-content">
                    <h3 class="card-title">${photo.rover.name}</h3>
                    <div class="card-meta">
                        <span class="meta-item"><i class="fas fa-camera"></i> ${photo.camera.full_name}</span>
                        <span class="meta-item"><i class="fas fa-calendar"></i> ${photo.earth_date}</span>
                        <span class="meta-item"><i class="fas fa-location-dot"></i> Sol ${photo.sol}</span>
                    </div>
                    <p class="card-description">Image captured by ${photo.camera.name} camera on ${photo.rover.name} rover.</p>
                </div>
            </div>
        `).join('');
    } catch (error) {
        grid.innerHTML = `<div class="card" style="grid-column: 1/-1;"><div class="error-message"><i class="fas fa-exclamation-circle"></i> Unable to load Mars rover data. Please try again.</div></div>`;
        console.error('Mars Rover Error:', error);
    }
}

// ==================== NEO FEED ====================

async function loadNeo() {
    const startDate = document.getElementById('startDateInput')?.value || '2024-01-01';
    const endDate = document.getElementById('endDateInput')?.value || '2024-01-07';
    const grid = document.getElementById('neoGrid');

    // Show loading state
    grid.innerHTML = `
        <div class="card" style="grid-column: 1/-1;">
            <div class="loader">
                <div class="spinner"></div>
                <p>Loading asteroid data...</p>
            </div>
        </div>
    `;

    try {
        const response = await fetch(`${API_BASE}/neo?start_date=${startDate}&end_date=${endDate}`);
        const data = await response.json();

        if (data.error) {
            grid.innerHTML = `<div class="card" style="grid-column: 1/-1;"><div class="error-message"><i class="fas fa-exclamation-circle"></i> ${data.message}</div></div>`;
            return;
        }

        const neoData = data.near_earth_objects || {};
        const asteroids = Object.values(neoData).flat();

        if (asteroids.length === 0) {
            grid.innerHTML = `<div class="card" style="grid-column: 1/-1;"><div class="error-message"><i class="fas fa-info-circle"></i> No asteroids found for the selected date range.</div></div>`;
            return;
        }

        // Sort by size (largest first)
        asteroids.sort((a, b) => {
            const sizeA = a.estimated_diameter.meters.estimated_diameter_max || 0;
            const sizeB = b.estimated_diameter.meters.estimated_diameter_max || 0;
            return sizeB - sizeA;
        });

        grid.innerHTML = asteroids.slice(0, 12).map(asteroid => {
            const diameter = asteroid.estimated_diameter.meters.estimated_diameter_max.toFixed(1);
            const hazardous = asteroid.is_potentially_hazardous_asteroid;
            const closeApproach = asteroid.close_approach_data[0];
            const velocity = closeApproach ? (parseFloat(closeApproach.relative_velocity.kilometers_per_hour) / 1000).toFixed(2) : 'N/A';

            return `
                <div class="card">
                    <div class="card-content">
                        <h3 class="card-title"><i class="fas fa-asteroid"></i> ${asteroid.name}</h3>
                        <div class="card-meta">
                            <span class="meta-item"><i class="fas fa-ruler"></i> ${diameter} m diameter</span>
                            <span class="meta-item"><i class="fas fa-tachometer-alt"></i> ${velocity}k km/s</span>
                        </div>
                        <p class="card-description">
                            <strong>Closest Approach:</strong> ${closeApproach?.close_approach_date_full || 'Unknown'}
                        </p>
                        <p class="card-description">
                            <strong>Min Distance:</strong> ${closeApproach ? (parseFloat(closeApproach.miss_distance.kilometers) / 1000000).toFixed(2) + ' Million km' : 'N/A'}
                        </p>
                        ${hazardous ? `<span class="badge hazard"><i class="fas fa-warning"></i> Potentially Hazardous</span>` : `<span class="badge safe"><i class="fas fa-check"></i> Not Hazardous</span>`}
                    </div>
                </div>
            `;
        }).join('');
    } catch (error) {
        grid.innerHTML = `<div class="card" style="grid-column: 1/-1;"><div class="error-message"><i class="fas fa-exclamation-circle"></i> Unable to load asteroid data. Please try again.</div></div>`;
        console.error('NEO Error:', error);
    }
}
