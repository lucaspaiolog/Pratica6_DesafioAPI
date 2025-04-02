package br.edu.fatecpg.api;

import br.edu.fatecpg.model.Post;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonPlaceholder {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. Consumo da API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();

        // 2. Conversão para objetos
        Gson gson = new Gson();
        List<Post> posts = gson.fromJson(jsonResponse, new TypeToken<List<Post>>(){}.getType());

        // 3. Operações com Streams e Lambdas

        // Filtragem: posts com "qui" no título (case insensitive)
        List<Post> postsFiltrados = posts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains("qui"))
                .toList();

        System.out.println(" POSTS FILTRADOS (contém 'qui' no título)");
        postsFiltrados.forEach(System.out::println);

        // Ordenação: por id crescente
        List<Post> sortedPosts = postsFiltrados.stream()
                .sorted(Comparator.comparingInt(Post::getId))
                .toList();

        System.out.println("\n POSTS ORDENADOS POR ID ");
        sortedPosts.forEach(System.out::println);

        // Agrupamento: contagem de posts por userId
        Map<Integer, Long> postsPorUsuario = postsFiltrados.stream()
                .collect(Collectors.groupingBy(
                        Post::getUserId,
                        Collectors.counting()
                ));

        System.out.println("\n CONTAGEM DE POSTS POR USER ID");
        postsPorUsuario.forEach((userId, count) ->
                System.out.println("User ID: " + userId + " - Posts: " + count));

        // Mapeamento: apenas títulos
        List<String> titulos = postsFiltrados.stream()
                .map(Post::getTitle)
                .toList();

        System.out.println("\n TÍTULOS DOS POSTS FILTRADOS");
        titulos.forEach(System.out::println);

        // Redução: soma dos IDs dos posts filtrados
        int somaFiltrados = postsFiltrados.stream()
                .mapToInt(Post::getId)
                .sum();

        System.out.println("\n SOMA DOS IDs DOS POSTS FILTRADOS");
        System.out.println("Total: " + somaFiltrados);
    }
}
