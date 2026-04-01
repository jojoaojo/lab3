import fitz  # PyMuPDF
import os

pdf_path = "lab3.pdf"
output_file = "PDF_ANALYSIS.txt"

print(f"📄 Analisando PDF: {pdf_path}")

doc = fitz.open(pdf_path)
total_pages = len(doc)

with open(output_file, "w", encoding="utf-8") as f:
    f.write("=" * 80 + "\n")
    f.write("ANÁLISE COMPLETA DO PDF - LAB3\n")
    f.write("=" * 80 + "\n\n")
    f.write(f"Total de páginas: {total_pages}\n\n")
    
    for page_num in range(total_pages):
        page = doc[page_num]
        f.write(f"\n{'=' * 80}\n")
        f.write(f"PÁGINA {page_num + 1}/{total_pages}\n")
        f.write(f"{'=' * 80}\n\n")
        
        # Extrair texto
        text = page.get_text()
        f.write("--- TEXTO ---\n")
        f.write(text)
        f.write("\n")
        
        # Extrair imagens
        images = page.get_images()
        if images:
            f.write(f"\n--- IMAGENS ENCONTRADAS: {len(images)} ---\n")
            for img_index, img in enumerate(images):
                xref = img[0]
                f.write(f"Imagem {img_index + 1}: xref={xref}\n")
                
                # Tentar extrair informações da imagem
                try:
                    base_image = doc.extract_image(xref)
                    f.write(f"  - Formato: {base_image['ext']}\n")
                    f.write(f"  - Tamanho: {len(base_image['image'])} bytes\n")
                    f.write(f"  - Dimensões: {base_image.get('width', 'N/A')}x{base_image.get('height', 'N/A')}\n")
                except:
                    f.write(f"  - Não foi possível extrair detalhes\n")
        
        # Extrair tabelas (aproximação usando blocos de texto)
        blocks = page.get_text("dict")["blocks"]
        tables_found = False
        for block in blocks:
            if "lines" in block:
                # Verificar se parece uma tabela
                for line in block["lines"]:
                    text_line = " ".join([span["text"] for span in line["spans"]])
                    if "|" in text_line or "\t" in text_line:
                        if not tables_found:
                            f.write("\n--- POSSÍVEIS TABELAS/ESTRUTURAS ---\n")
                            tables_found = True
                        f.write(text_line + "\n")
        
        print(f"✓ Página {page_num + 1}/{total_pages} processada")
    
    f.write("\n" + "=" * 80 + "\n")
    f.write("FIM DA ANÁLISE\n")
    f.write("=" * 80 + "\n")

doc.close()

print(f"\n✅ Análise completa! Resultado salvo em: {output_file}")
print(f"📊 Total de páginas analisadas: {total_pages}")
