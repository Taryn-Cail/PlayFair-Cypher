import java.util.Scanner;

/*******************************************************************
 *  PlayFair Cypher Encryption
 *
 *  Purpose: To encrypt a given word and key into a PlayFair
 *  Cypher.
 *
 *  The value to be encrypted is split into pairs of two letters.
 *
 *
 *  --- Rules of PlayFair Cypher ---
 *  Given a keyword, a 5x5 grid will be generated using the
 *  leftover letters in the alphabet with no repetitions.
 *  J is usually omitted.
 *
 *  When encrypting...
 *  If both letters are in the same column take the letter
 *  below each one.
 *
 *  If both letters are in the same row take the letter to
 *  the right of each one. Will wrap around if at the left-most
 *  of the square.
 *
 *  Otherwise, form a rectangle, reading left to right.
 *
 ******************************************************************/
class PlayFairCypher
{

    public static void main(String[] args)
    {
        // Declaring Scanner for user input
        Scanner input = new Scanner(System.in);

        // Asking user for Key to create the grid
        System.out.println("Please enter key: ");
        String stringKey = input.next();

        // Asking user for value to be encrypted
        System.out.println("Please enter value to be encrypted: ");
        String stringValue = input.next();

        // Converting to String Builder
        // StringBuilder is used because it is easier to
        // change and manipulate
        StringBuilder key = new StringBuilder(stringKey);
        StringBuilder str = new StringBuilder(stringValue);

        // Displaying the values
        System.out.println("Key text: " + key);
        System.out.println("Value text: " + str);
        encryptByPlayfairCipher(str, key);
        System.out.println("Cypher text: " + str);
    }

    // Function to encrypt using Playfair Cipher
    static void encryptByPlayfairCipher(StringBuilder str, StringBuilder key)
    {
        // Create matrix for cypher
        char[][] keyT = new char[5][5];

        // Preparing the Key and Value
        removeSpaces(key);
        removeSpaces(str);
        toLowerCase(key);
        toLowerCase(str);

        // Making sure words are even, adding x as a "bogus" value
        prepare(str);

        // Generating the Grid
        generateKeyTable(key, keyT);

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