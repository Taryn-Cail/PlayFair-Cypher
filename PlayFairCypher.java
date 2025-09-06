import java.util.Scanner;

/*******************************************************************
 * @author - Taryn Cail
 * @date - September 2025
 *
 *  PlayFair Cypher Encryption
 *
 *  Purpose: To encrypt a given word using the PlayFair Cipher.
 *           The cipher generates a 5x5 letter grid (excluding 'j'),
 *           then uses a set of rules to encode pairs of letters.
 *
 *  Rules of PlayFair Cipher:
 * 1) Generate a 5x5 grid with unique letters (excluding 'j').
 *    The key for the grid is based on the input word, followed by
 *    the rest of the alphabet, skipping duplicates.
 *
 * 2) Replace 'j' with 'i'.
 *
 * 3) Split the input text into pairs of letters.
 *    If there's an odd number of letters, add a filler (e.g., 'z').
 *
 * 4) For each pair, apply the following encryption rules:
 *    - Same column: Take the letter below each (wrap around if needed).
 *    - Same row: Take the letter to the right of each (wrap around).
 *    - Rectangle: Swap the column values between the two letters.
 *
 * Credit to GeeksforGeeks and National Treasure for the idea.
 ******************************************************************/
class PlayFairCypher
{

    public static void main(String[] args)
    {
        // Declaring Scanner for user input
        Scanner input = new Scanner(System.in);

        // Asking user for value to be encrypted
        System.out.println("Please enter value to be encrypted: ");
        String stringValue = input.next();

        // Converting to String Builder
        // StringBuilder is used because it is easier to change and manipulate
        StringBuilder str = new StringBuilder(stringValue);

        // Displaying the values
        System.out.println("Value text: " + str);
        encryptByPlayfairCipher(str);
        System.out.println("Cypher text: " + str);
    }

    // Function to encrypt using Playfair Cipher
    static void encryptByPlayfairCipher(StringBuilder str)
    {
        // Create matrix for cypher
        char[][] keyT = new char[5][5];

        // Preparing the Value
        removeSpaces(str);
        toLowerCase(str);

        // Making sure words are even, adding x as a "bogus" value
        prepare(str);

        // Generating the Grid
        generateKeyTable(str, keyT);

        // Printing the Grid
        printKeyTable(keyT);

        // Encrypting the code into table
        encrypt(str, keyT);
    }

    // Function to remove all spaces in a string
    static void removeSpaces(StringBuilder plain)
    {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < plain.length(); i++)
        {
            if (plain.charAt(i) != ' ')
            {
                temp.append(plain.charAt(i));
            }
        }
        plain.setLength(0);
        plain.append(temp);
    }

    // Function to convert the string to lowercase
    static void toLowerCase(StringBuilder plain)
    {
        for (int i = 0; i < plain.length(); i++)
        {
            if (plain.charAt(i) > 64 && plain.charAt(i) < 91)
            {
                plain.setCharAt(i, (char) (plain.charAt(i) + 32));
            }
        }
    }

    // Function to make the plain text length to be even
    static int prepare(StringBuilder str)
    {
        if (str.length() % 2 != 0)
        {
            str.append('z');
        }
        return str.length();
    }

    // Function to generate the 5x5 key square
    static void generateKeyTable(StringBuilder key, char[][] keyT)
    {
        int n = key.length();

        int[] hash = new int[26];

        for (int i = 0; i < n; i++)
        {
            if (key.charAt(i) != 'j')
                hash[key.charAt(i) - 97] = 2;
        }

        hash['j' - 97] = 1;

        int i = 0, j = 0;

        for (int k = 0; k < n; k++)
        {
            if (hash[key.charAt(k) - 97] == 2)
            {
                hash[key.charAt(k) - 97] -= 1;
                keyT[i][j++] = key.charAt(k);
                if (j == 5)
                {
                    i++;
                    j = 0;
                }
            }
        }

        for (int k = 0; k < 26; k++)
        {
            if (hash[k] == 0)
            {
                keyT[i][j++] = (char)(k + 97);
                if (j == 5)
                {
                    i++;
                    j = 0;
                }
            }
        }
    }

    static void printKeyTable(char[][] keyT)
    {
        System.out.println("\nGenerated 5x5 Key Table:");
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                System.out.print(keyT[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Function to search for the characters of a digraph
    // in the key square and return their position
    static void search(char[][] keyT, char a, char b, int[] arr)
    {
        if (a == 'j') a = 'i';
        if (b == 'j') b = 'i';

        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if (keyT[i][j] == a)
                {
                    arr[0] = i;
                    arr[1] = j;
                } else if (keyT[i][j] == b)
                {
                    arr[2] = i;
                    arr[3] = j;
                }
            }
        }
    }



    // Function for performing the encryption
    static void encrypt(StringBuilder str, char[][] keyT)
    {
        int[] arr = new int[4];
        for (int i = 0; i < str.length(); i += 2)
        {
            search(keyT, str.charAt(i), str.charAt(i + 1), arr);

            if (arr[0] == arr[2])
            {
                str.setCharAt(i, keyT[arr[0]][(arr[1] + 1) % 5]);
                str.setCharAt(i + 1, keyT[arr[0]][(arr[3] + 1) % 5]);
            }
            else if (arr[1] == arr[3])
            {
                str.setCharAt(i, keyT[(arr[0] + 1) % 5][arr[1]]);
                str.setCharAt(i + 1, keyT[(arr[2] + 1) % 5][arr[1]]);
            }
            else
            {
                str.setCharAt(i, keyT[arr[0]][arr[3]]);
                str.setCharAt(i + 1, keyT[arr[2]][arr[1]]);
            }
        }
    }
}