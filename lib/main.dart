import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'services/native_bridge.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: const HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String status = 'No file selected';

  Future<void> pickFile() async {
    final result = await FilePicker.pickFiles();

    if (result == null) return;

    final path = result.files.single.path!;

    setState(() {
      status = 'Processing file...';
    });

    final response = await NativeBridge.processFile(path);

    setState(() {
      status = response ?? 'Done';
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Ghost Vault'),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(status),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: pickFile,
                child: const Text('Select File'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}