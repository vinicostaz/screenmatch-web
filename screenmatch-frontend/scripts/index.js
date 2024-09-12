import getDados from "./getDados.js";

// Map DOM elements that you want to update
const elementos = {
    top5: document.querySelector('[data-name="top5"]'),
    releases: document.querySelector('[data-name="releases"]'),
    series: document.querySelector('[data-name="series"]')
};

// Function to create the movie list
function criarListaFilmes(elemento, dados) {
    const ulExistente = elemento.querySelector('ul');

    if (ulExistente) {
        elemento.removeChild(ulExistente);
    }

    const ul = document.createElement('ul');
    ul.className = 'lista';
    const listaHTML = dados.map((filme) => `
        <li>
            <a href="/detalhes.html?id=${filme.id}">
                <img src="${filme.poster}" alt="${filme.title}">
            </a>
        </li>
    `).join('');

    ul.innerHTML = listaHTML;
    elemento.appendChild(ul);
}

// Generic error handler
function lidarComErro(mensagemErro) {
    console.error(mensagemErro);
}

const categoriaSelect = document.querySelector('[data-categorias]');
const sectionsParaOcultar = document.querySelectorAll('.section'); // Add the CSS class 'hide-when-filtered' to sections you want hidden.

categoriaSelect.addEventListener('change', function () {
    const categoria = document.querySelector('[data-name="categoria"]');
    const categoriaSelecionada = categoriaSelect.value;

    if (categoriaSelecionada === 'all') {
        sectionsParaOcultar.forEach(section => section.classList.remove('hidden'));
        categoria.classList.add('hidden');
    } else {
        sectionsParaOcultar.forEach(section => section.classList.add('hidden'));
        categoria.classList.remove('hidden');
        // Request the category-specific endpoint
        getDados(`/series/category/${categoriaSelecionada}`)
            .then(data => {
                criarListaFilmes(categoria, data);
            })
            .catch(error => {
                lidarComErro("Error loading category data.");
            });
    }
});

// Function to generate series requests
function geraSeries() {
    const urls = ['/series/top5', '/series/releases', '/series'];

    Promise.all(urls.map(url => getDados(url)))
        .then(data => {
            criarListaFilmes(elementos.top5, data[0]);
            criarListaFilmes(elementos.releases, data[1]);
            criarListaFilmes(elementos.series, data[2].slice(0, 5));
        })
        .catch(error => {
            lidarComErro("Error loading data.");
        });
}

// Load series lists on page load
geraSeries();
